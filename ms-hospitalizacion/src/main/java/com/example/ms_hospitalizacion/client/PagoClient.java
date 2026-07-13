package com.example.ms_hospitalizacion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "ms-pagos-convenios")
public interface PagoClient {

    @PostMapping("/api/pagos/procesar")
    void procesarCobro(@RequestBody Map<String, Object> request);

}