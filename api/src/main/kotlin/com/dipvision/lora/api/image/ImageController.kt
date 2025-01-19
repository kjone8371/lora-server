package com.dipvision.lora.api.image

import com.dipvision.lora.business.image.dto.ImageDto
import com.dipvision.lora.business.image.service.ImageService
import com.dipvision.lora.common.response.ResponseData
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URLConnection


@RestController
@RequestMapping("/images")
@SecurityRequirement(name = "Authorization")
class ImageController(
    private val imageService: ImageService,
) {

//    @CrossOrigin(origins = ["https://4b78-218-233-244-111.ngrok-free.app/images/{uuid}", "https://the-one-led.vercel.app/images/{uuid}"])
    @GetMapping("/{uuid}")
    fun getImageInfo(@PathVariable uuid: String): ResponseEntity<ResponseData<ImageDto>> {
        val data = imageService.getImageInfo(uuid)
        return ResponseData.ok(data = data)
    }

//    @CrossOrigin(origins = ["https://4b78-218-233-244-111.ngrok-free.app/images/{uuid}/file", "https://the-one-led.vercel.app/images/{uuid}/file"])
    @GetMapping("/{uuid}/file")
    fun readImage(@PathVariable uuid: String): ResponseEntity<ByteArray> {
        val info = imageService.getImageInfo(uuid)
        val data = imageService.readImage(uuid)

        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(info.fileName)))
            .contentLength(data.size.toLong())
            .body(data)
    }
}