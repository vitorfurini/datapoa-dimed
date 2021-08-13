package com.datapoamobilidade.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/linhas")
@Tag(name = "linhas")
public class LinhaController {
}
