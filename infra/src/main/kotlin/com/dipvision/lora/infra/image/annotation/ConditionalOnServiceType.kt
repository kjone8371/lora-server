package com.dipvision.lora.infra.image.annotation

import com.dipvision.lora.infra.image.ManagerType
import org.springframework.context.annotation.Conditional

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Conditional(OnServiceTypeCondition::class)
annotation class ConditionalOnServiceType(val value: ManagerType)
