package com.library.service;
import java.util.List;

 import com.library.entity.Fine;
public interface FineService { 
    List<Fine> myFines(Long memberId); 
    List<Fine> all(); Fine markPaid(Long id); 
}