package com.example.techpowerhousebackend.order;
import com.example.techpowerhousebackend.support.exceptions.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements OrderServiceInterface {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Metodo per trovare un ordine per ID
    @Override
    @Transactional(readOnly = true)
    public Order findOne(int id) throws OrderNotFoundException {
        Order order = this.orderRepository.findById(id);
        // se l'ordine non esiste, lancio un'eccezione
        if(order == null) {
            throw new OrderNotFoundException();
        }
        return order;
    }

    // Metodo per trovare tutti gli ordini
    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        // creazione di una pagina di ordini ordinata per data di creazione discendente
        Page<Order> pagedResult = this.orderRepository.findAllByOrderByCreateTimeDesc(pageable);
        // se la pagina non è vuota, restituisco gli ordini, altrimenti restituisco una lista vuota
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    // Metodo per trovare gli ordini di un utente
    @Override
    @Transactional(readOnly = true)
    public List<Order> findByUser(String email, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        // creazione di una pagina di ordini per l'utente specificato, ordinata per data di creazione discendente
        Page<Order> pagedResult = this.orderRepository.findByUserEmailOrderByCreateTimeDesc(email, pageable);
        // Se la pagina non è vuota, restituisco gli ordini, altrimenti restituisco una lista vuota
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }
}

