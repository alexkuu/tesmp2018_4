package com.alex.app.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource

@Configuration
@ImportResource("classpath*:app-context.xml")
class XmlCondifuration {
}
