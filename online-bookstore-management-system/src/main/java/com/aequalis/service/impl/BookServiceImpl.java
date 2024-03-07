package com.aequalis.service.impl;

import com.aequalis.dto.BookDto;
import com.aequalis.entity.Book;
import com.aequalis.entity.CurrentSession;
import com.aequalis.entity.UserInfo;
import com.aequalis.repository.BookRepository;
import com.aequalis.service.BookService;
import com.aequalis.utils.BookMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LogManager.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CurrentSession currentSession;

    @Override
    public String addNewBook(BookDto bookDto) {

        String indianRupees=String.valueOf(getIndianRupees(bookDto.getPrice()));
        bookDto.setPrice(indianRupees);
        Book book=BookMapper.INSTANCE.mapToBook(bookDto);
        book.setPrice(formatDecimal(book.getPrice()));
         bookRepository.save(book);
        logger.info("Book added successfully");
        return "Book added successfully";
    }

    @Override
    public BookDto updateBookDetail(BookDto bookDto) {
        Book book=null;
            logger.info("check book is exist or not");
            book=bookRepository.findById(bookDto.getId()).get();
            if(book!=null) {
                logger.info("convert bookDTO to book");
                String indianRupees=String.valueOf(getIndianRupees(bookDto.getPrice()));
                bookDto.setPrice(indianRupees);
                book.setPrice(formatDecimal(Double.valueOf(indianRupees)));
                book = BookMapper.INSTANCE.mapToBook(bookDto);
            }
           else
            {
                logger.error("not value found");
                throw new NoSuchElementException();
        }
           logger.info("book updated and convert book to bookDTO");
         BookMapper.INSTANCE.mapToBookDto(bookRepository.save(book));
         return convertCurrency(book);
    }

    private Double formatDecimal(Double number)
    {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        Double formateNumber= Double.valueOf(decimalFormat.format(number));
        return formateNumber;
    }

    private Double getIndianRupees(String rupees)
    {
        char ch=rupees.charAt(0);
        Double amount= Double.valueOf(rupees.substring(1));
        switch (ch){
            case '$':
                return amount*82.83;

        }
     return 0.0;
    }

    @Override
    public List<BookDto> viewAllBooks() {
            List<Book> bookList= bookRepository.findAll();
            List<BookDto> bookDtoList=new ArrayList<>();
            if(bookList.size()>0)
            {
                for(Book book:bookList)
                {
                    bookDtoList.add(convertCurrency(book));
                }
                return bookDtoList;
            }
            throw new NoSuchElementException("Books is empty!");
        }

    @Override
    public BookDto viewBook(UUID id) {
       try{
           return convertCurrency(bookRepository.findById(id).get());
       }
       catch (NoSuchElementException ex)
       {
           throw new NoSuchElementException("Books has not found!");
       }
    }

    public BookDto convertCurrency(Book book)
    {
        String[] isoCountries = Locale.getISOCountries();
        Map<String,String> countries=new HashMap<>();
        for (String country : isoCountries) {
            Locale locale = new Locale("en", country);
            String code = locale.getCountry();
            String name = locale.getDisplayCountry();
            countries.put(name,code);
        }

        UserInfo userInfo=currentSession.getUserInfo();
        String countryCode=countries.get(userInfo.getCountry());
        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(new Locale(Locale.ENGLISH.getLanguage(), countryCode));
        double conversionRate = getExchangeRate(countryCode);
        Double number= Double.valueOf(book.getPrice())*conversionRate;
        String price=numberFormat.format(number);
        return BookDto.builder()
                .date(book.getDate())
                .title(book.getTitle())
                .author(book.getAuthor())
                .quantity(book.getQuantity())
                .description(book.getDescription())
                .publication(book.getPublication())
                .id(book.getId())
                .price(price).build();

    }

    private Double getExchangeRate(String countryCode)
    {
       switch (countryCode.toUpperCase()){
           case "CN":
               return 11.51;
           case "CA":
               return 61.27;
           case "KW":
               return 269.27;
           case "US":
               return 82.85;
           case "IT":
               return 90.12;
       }
       return 0.0;
    }

    @Override
    public String deleteBook(UUID id) {
       try
       {
           bookRepository.deleteById(id);
           return id+" Book delete successfully";
       }
       catch (NoSuchElementException ex)
       {
           throw new NoSuchElementException("Books has not found!");
       }
    }
}
