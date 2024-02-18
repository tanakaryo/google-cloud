package com.myapp.convfl.service;

public interface Service<I,O> {
    
    O execute(I input) throws Exception;
}
