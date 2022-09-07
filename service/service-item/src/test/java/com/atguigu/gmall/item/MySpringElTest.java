package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Author：张世平
 * Date：2022/9/7 10:11
 */
public class MySpringElTest {
    @Test
    public void testS() {
        int par[] =new int[]{1,2,3};
        ExpressionParser parser=new SpelExpressionParser();

        Expression expression = parser.parseExpression("info:#{#parms[0]}", new TemplateParserContext());

        StandardEvaluationContext context=new StandardEvaluationContext();
        context.setVariable("parms",par);

        String value = expression.getValue(context, String.class);
        System.out.println(value);


    }
}
