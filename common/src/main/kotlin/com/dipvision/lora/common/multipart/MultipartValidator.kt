package dev.jombi.blog.common.multipart

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.apache.tika.detect.DefaultDetector
import org.apache.tika.detect.Detector
import org.apache.tika.metadata.Metadata
import org.springframework.http.MediaType
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedInputStream
import kotlin.properties.Delegates

class MultipartValidator : ConstraintValidator<ValidImageFile, MultipartFile> {
    private var allowedTypes by Delegates.notNull<List<MediaType>>()
    private var maxSizeInBytes by Delegates.notNull<Long>()

    override fun initialize(constraintAnnotation: ValidImageFile) {
        allowedTypes = constraintAnnotation.allowedTypes.map { MediaType.parseMediaType(it) }
        maxSizeInBytes = constraintAnnotation.maxSizeInByte
    }

    override fun isValid(file: MultipartFile, context: ConstraintValidatorContext): Boolean {
        val validatedFile = file.takeIf { !it.isEmpty }
        if (validatedFile?.contentType == null) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("Invalid file received.")
                .addConstraintViolation()
            return false
        }

        if (file.size > maxSizeInBytes) {
            context.buildConstraintViolationWithTemplate("File size cannot exceed ${maxSizeInBytes / 1024 / 1024}MB.")
                .addConstraintViolation()
            return false
        }
        val detected = detectContentType(BufferedInputStream(file.inputStream))
        if (allowedTypes.none { it.isCompatibleWith(detected) }) {
            context.buildConstraintViolationWithTemplate("Content-Type cannot be '$detected'. " +
                    "(excepted ${allowedTypes.joinToString(", ") { "$it" }}")
                .addConstraintViolation()
            return false
        }

        return true
    }

    private fun detectContentType(stream: BufferedInputStream): MediaType {
        val detector: Detector = DefaultDetector()
        val metadata = Metadata()

        return MediaType.parseMediaType(detector.detect(stream, metadata).toString())
    }
}
