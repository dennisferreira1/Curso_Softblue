package br.com.sw2you.realmeet.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseEntityUtils {

    private ResponseEntityUtils() {}

    public static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public static <T> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public static ResponseEntity<Object> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public static ResponseEntity<Void> noContent(Void aVoid) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
