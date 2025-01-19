package com.dipvision.lora.infra.image.annotation

import com.dipvision.lora.infra.image.ManagerType
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

class OnServiceTypeCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val properties = context.environment
        val actual = ManagerType.valueOf(properties.getProperty("app.image.manager-type")?.uppercase() ?: "")

        val excepted = metadata
            .annotations[ConditionalOnServiceType::class.java]
            .getEnum("value", ManagerType::class.java)

        return excepted == actual
    }
}
