package com.aequalis.service.impl;

import com.aequalis.dto.BookDto;
import com.aequalis.dto.CartDto;
import com.aequalis.entity.Book;
import com.aequalis.entity.CurrentSession;
import com.aequalis.entity.ShoppingCart;
import com.aequalis.entity.UserInfo;
import com.aequalis.exception.NoSuchBookExistsException;
import com.aequalis.repository.BookRepository;
import com.aequalis.repository.ShoppingRepository;
import com.aequalis.service.ShoppingCartService;
import com.aequalis.utils.BookMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingRepository shoppingRepository;

    @Autowired
    private CurrentSession currentSession;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookRepository bookRepository;




    private static final Logger logger = LogManager.getLogger(ShoppingCartServiceImpl.class);
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<CartDto>  addToCart(UUID id, ShoppingCart shoppingCart) {
        UserInfo userInfo=currentSession.getUserInfo();
        shoppingCart.setId(UUID.randomUUID());
        Book book=bookRepository.findById(id).get();
        if(book.getQuantity()>=shoppingCart.getQuantity()) {

            book.setQuantity(book.getQuantity()-shoppingCart.getQuantity());
            BookDto bookDto= BookMapper.INSTANCE.mapToBookDto(book);
            shoppingCart.setUserInfo(userInfo);
            shoppingCart.setBook(book);
            shoppingRepository.save(shoppingCart);
            return viewAllCartDetails();
        }
        else {
          throw new NoSuchBookExistsException("Out of Stock");
        }
    }
    private List<CartDto> viewAllCartDetails()
    {
        List<ShoppingCart> shoppingCartList=shoppingRepository.findAll();
        List<CartDto> cartDtos=new ArrayList<>();
        for(ShoppingCart shoppingCart:shoppingCartList)
        {
            BookDto book=bookService.convertCurrency(shoppingCart.getBook());
            cartDtos.add(BookMapper.INSTANCE.mapToCartDto(book));
        }
        return cartDtos;
    }
}
