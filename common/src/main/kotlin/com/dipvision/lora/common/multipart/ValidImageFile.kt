package dev.jombi.blog.common.multipart

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MultipartValidator::class])
annotation class ValidImageFile(
    val allowedTypes: Array<String> = [],
    val maxSizeInByte: Long = 10 * 1024 * 1024,
    val message: String = "That Content-Type is not allowed.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
