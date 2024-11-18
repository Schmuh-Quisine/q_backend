package org.schmuh.quisine.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
@NoArgsConstructor
@AllArgsConstructor
public class OCRService {
    @Value("${ocrPath}")
    private String ocrPath;
}
