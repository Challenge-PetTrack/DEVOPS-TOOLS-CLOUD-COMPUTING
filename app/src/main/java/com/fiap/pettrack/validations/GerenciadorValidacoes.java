package com.fiap.pettrack.validations;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GerenciadorValidacoes {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> 
	gerenciarValidacoes(MethodArgumentNotValidException ex){
		
		Map<String,String> erros = new HashMap<String, String>();
		
		ex.getBindingResult().getFieldErrors().forEach(erro -> {
			erros.put(erro.getField(), erro.getDefaultMessage());
		});
		
		return ResponseEntity.badRequest().body(erros);
		
	}

}
