package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 张世平哒
* @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
* @createDate 2022-08-27 09:53:41
*/
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService{

    @Resource
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Resource
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAttrInfoAndValueById(Long c1Id, Long c2Id, Long c3Id) {
        List<BaseAttrInfo> infos = baseAttrInfoMapper.getAttrInfoAndValueById(c1Id, c2Id, c3Id);
        return infos;
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        if (baseAttrInfo.getId()==null){
            //添加操作
            saveAttrInfoAndValue(baseAttrInfo);
        }else {
            //更新操作
            //1:进行删除操作，进行了更新操作，
            updateInfoAndValue(baseAttrInfo);
        }
    }

    private void updateInfoAndValue(BaseAttrInfo baseAttrInfo) {
        baseAttrInfoMapper.updateById(baseAttrInfo);
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

        List<Long> ids = attrValueList.stream()
                .map(BaseAttrValue::getId).collect(Collectors.toList());

        if (ids.size()>0){
            //删除前端没有传进来的id
            // delete * from base_attr_value where attr_id=11 and id not in(59,61)
            QueryWrapper<BaseAttrValue> delWrapper = new QueryWrapper<>();
            delWrapper.eq("attr_id", baseAttrInfo.getId());
            delWrapper.notIn("id",ids);
            baseAttrValueMapper.delete(delWrapper);
        }else {
            //前端没有传过来，删除这个属性下面的所有属性值
            QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("attr_id", baseAttrInfo.getId());
            baseAttrValueMapper.delete(queryWrapper);
        }

        for (BaseAttrValue attrVale : attrValueList) {
            if (attrVale.getId()==null){
                baseAttrValueMapper.insert(attrVale);
            }else if(attrVale.getAttrId()!=null){
                baseAttrValueMapper.updateById(attrVale);
            }
        }
    }

    //给一级分类添加属性和属性值
    private void saveAttrInfoAndValue(BaseAttrInfo baseAttrInfo) {
        //给属性表添加字段
        baseAttrInfoMapper.insert(baseAttrInfo);
        //获取属性值id
        Long id = baseAttrInfo.getId();
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();


        attrValueList.forEach(e->{
            e.setAttrId(id);
            baseAttrValueMapper.insert(e);
        });
    }
}




