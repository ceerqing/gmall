package com.atguigu.gmall.seacher.service.impl;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.vo.search.OrderMapVo;
import com.atguigu.gmall.model.vo.search.SearchParamVo;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;
import com.atguigu.gmall.seacher.repository.GoodsRepository;
import com.atguigu.gmall.seacher.service.GoodsService;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：张世平
 * Date：2022/9/9 15:57
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    ElasticsearchRestTemplate esRestTemplate;

    @Override
    public void save(Goods goods) {
        goodsRepository.save(goods);

    }

    @Override
    public void delete(Long  skuId) {
       goodsRepository.deleteById(skuId);
    }

    @Override
    public SearchResponseVo search(SearchParamVo searchParamVo) {
        //构建生成语句
        Query query = getQueryBoolCondition(searchParamVo);
        SearchHits<Goods> goods = esRestTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));
        //从es中查询数据转成前端需要的数据
        SearchResponseVo showVo = changeToSearchRespVo(goods,searchParamVo);
        return showVo;
    }

    //前端展示的数据
    private SearchResponseVo changeToSearchRespVo(SearchHits<Goods> goods,SearchParamVo paramVo) {
        SearchResponseVo showVo = new SearchResponseVo();
        showVo.setSearchParam(paramVo);
        //2、构建品牌面包屑 trademark=1:小米
        String trademark = paramVo.getTrademark();
        if (!StringUtils.isEmpty(trademark)){
            String mark="品牌:"+trademark.split(":")[1];
            showVo.setTrademarkParam(mark);
        }

        //3、平台属性面包屑
        if(paramVo.getProps()!= null && paramVo.getProps().length>0){
            List<SearchAttr> propsParamList = new ArrayList<>();
            for (String prop : paramVo.getProps()) {
                //23:8G:运行内存
                String[] split = prop.split(":");
                //一个SearchAttr 代表一个属性面包屑
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(Long.parseLong(split[0]));
                searchAttr.setAttrValue(split[1]);
                searchAttr.setAttrName(split[2]);
                propsParamList.add(searchAttr);
            }
            showVo.setPropsParamList(propsParamList);
        }

        //TODO 4、所有品牌列表 。需要ES聚合分析
        showVo.setTrademarkList(Lists.newArrayList());
        //TODO 5、所有属性列表 。需要ES聚合分析
        showVo.setAttrsList(Lists.newArrayList());


        //6、返回排序信息  order=1:desc
        if(!org.springframework.util.StringUtils.isEmpty(paramVo.getOrder())){
            String order = paramVo.getOrder();
            OrderMapVo mapVo = new OrderMapVo();
            mapVo.setType(order.split(":")[0]);
            mapVo.setSort(order.split(":")[1]);
            showVo.setOrderMap(mapVo);
        }
        //7、所有搜索到的商品列表
        List<Goods> goodsList = new ArrayList<>();
        List<SearchHit<Goods>> searchHits = goods.getSearchHits();
        for (SearchHit<Goods> searchHit : searchHits) {
            Goods goodsDemo = searchHit.getContent();
            //可能会有高亮显示
            if (!StringUtils.isEmpty(paramVo.getKeyword())){
                String lightTitle = searchHit.getHighlightField("title").get(0);
                goodsDemo.setTitle(lightTitle);
            }
            goodsList.add(goodsDemo);
        }
        showVo.setGoodsList(goodsList);

        //显示页码
        showVo.setPageNo(paramVo.getPageNo());

        //总页码
        //所有的命中条数
        long totalHits = goods.getTotalHits();
        //设置的是一页显示的条数为8
        int totalPage= (int) (totalHits%8==0?totalHits/8:(totalHits/8)+1);
        showVo.setTotalPages(totalPage);

        String url = makeUrlParam(paramVo);
        showVo.setUrlParam(url);
        return showVo;
    }

    private String makeUrlParam(SearchParamVo paramVo) {
        StringBuilder builder = new StringBuilder("list.html?");
        //1、拼三级分类所有参数
        if(paramVo.getCategory1Id()!=null){
            builder.append("&category1Id="+paramVo.getCategory1Id());
        }
        if(paramVo.getCategory2Id()!=null){
            builder.append("&category2Id="+paramVo.getCategory2Id());
        }
        if(paramVo.getCategory3Id()!=null){
            builder.append("&category3Id="+paramVo.getCategory3Id());
        }

        //2、拼关键字
        if(!org.springframework.util.StringUtils.isEmpty(paramVo.getKeyword())){
            builder.append("&keyword="+paramVo.getKeyword());
        }

        //3、拼品牌
        if(!org.springframework.util.StringUtils.isEmpty(paramVo.getTrademark())){
            builder.append("&trademark="+paramVo.getTrademark());
        }

        //4、拼属性
        if(paramVo.getProps()!=null && paramVo.getProps().length >0){
            for (String prop : paramVo.getProps()) {
                //props=23:8G:运行内存
                builder.append("&props="+prop);
            }
        }

        //拿到最终字符串
        String url = builder.toString();
        return url;

    }


    //按照条件在es中去查询数据的es查询语句
    private Query getQueryBoolCondition(SearchParamVo paramVo){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(paramVo.getTrademark())){
            boolQuery.must(QueryBuilders.
                    termQuery("category1Id",paramVo.getCategory1Id()));
        }
        if (!StringUtils.isEmpty(paramVo.getTrademark())){
            boolQuery.must(QueryBuilders.
                    termQuery("category2Id",paramVo.getCategory2Id()));
        }
        if (!StringUtils.isEmpty(paramVo.getTrademark())){
            boolQuery.must(QueryBuilders.
                    termQuery("category3Id",paramVo.getCategory3Id()));
        }
        //关键字
        if (!StringUtils.isEmpty(paramVo.getKeyword())){
            boolQuery.must(QueryBuilders.
                    matchQuery("title",paramVo.getKeyword()));
        }

        //2.3）、前端传了品牌 trademark=4:小米

        if (!StringUtils.isEmpty(paramVo.getTrademark())){
            Long tmId=Long.parseLong(paramVo.getTrademark().split(":")[0]);
            boolQuery.must(QueryBuilders.
                    matchQuery("tmId",tmId));
        }

        //2.4）、前端传了属性 props=4:128GB:机身存储&props=5:骁龙730:CPU型号

        if (paramVo.getProps()!=null&&paramVo.getProps().length!=0){
            String[] props = paramVo.getProps();
            for (String prop : props) {
                String[] flatPro = prop.split(":");
                Long attrId=Long.parseLong(flatPro[0]);
                String attrValue=flatPro[1];

                BoolQueryBuilder nest = QueryBuilders.boolQuery();
                nest.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                nest.must(QueryBuilders.termQuery("attrs.attrValue",attrValue));
                //嵌入式查询
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nest, ScoreMode.None);
                boolQuery.must(nestedQuery);
            }
        }
        ////========查询条件结束



        Query query =new NativeSearchQuery(boolQuery);

        //========排序
        //2.5）、前端传了排序 order=2:asc

        String order = paramVo.getOrder();
        String orderFiledFlag = order.split(":")[0];

        //根据哪一个字段进行排序

        String orderFiled="";
        switch (orderFiledFlag){
            case "1": orderFiled = "hotScore";break;
            case "2": orderFiled = "price";break;
            case "3": orderFiled = "createTime";break;
            default: orderFiled = "hotScore";
        }
        //根据哪一个字段进行排序
        Sort sort = Sort.by(orderFiled);

        if (order.split(":")[1].equals("asc")){
            sort = sort.ascending();
        }else {
            sort = sort.descending();
        }
        query.addSort(sort);

        //#添加分页
        PageRequest pageRequest = PageRequest.of(paramVo.getPageNo() - 1, 8);
        query.setPageable(pageRequest);

        //#################关键字的高亮显示,高亮查询
        if (!StringUtils.isEmpty(paramVo.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title").preTags("<span style='color:red'>")
                    .postTags("</span>");
            HighlightQuery highlightQuery = new HighlightQuery(highlightBuilder);
            query.setHighlightQuery(highlightQuery);
        }


        //todo 聚合操作

        return query;
    }


}
