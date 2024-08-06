package com.github.poldroc.ioc.test.model;

public class User {

    private Book book;

    private String name;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "book=" + book +
                ", name='" + name + '\'' +
                '}';
    }

}
