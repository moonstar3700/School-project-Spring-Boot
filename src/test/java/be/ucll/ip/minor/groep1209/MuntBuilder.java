package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.domain.model.Munt;

public class MuntBuilder {

    private long id = 0;
    private String name;
    private String country;
    private String currency;
    private Double value;
    private Integer year;

    private MuntBuilder(){}

    public static MuntBuilder aMunt() {return new MuntBuilder();}

    public MuntBuilder withId(long id){
        this.id = id;
        return this;
    }

    public MuntBuilder withName(String name){
        this.name = name;
        return this;
    }

    public MuntBuilder withCountry(String country){
        this.country = country;
        return this;
    }

    public MuntBuilder withCurrency(String currency){
        this.currency = currency;
        return this;
    }

    public MuntBuilder withValue(Double value){
        this.value = value;
        return this;
    }

    public MuntBuilder withYear(Integer year){
        this.year = year;
        return this;
    }

    public static MuntBuilder aValidMuntTestmunt(){
        return aMunt().withName("Testmunt").withCountry("Belgium").withCurrency("euro").withValue(3.2).withYear(1500);
    }

    public static MuntBuilder aValidMuntAnothermunt(){
        return aMunt().withName("Anothermunt").withCountry("Netherlands").withCurrency("euro").withValue(1.2).withYear(350);
    }

    public static MuntBuilder aValidMuntYetanothermunt(){
        return aMunt().withName("Yetanothermunt").withCountry("France").withCurrency("euro").withValue(1.5).withYear(680);
    }

    public static MuntBuilder aValidMuntToomanymunten(){
        return aMunt().withName("Toomanymunten").withCountry("USA").withCurrency("euro").withValue(0.9).withYear(1630);
    }

    public static MuntBuilder aValidMuntWithDuplicateName(){
        return aMunt().withName("Testmunt").withCountry("France").withCurrency("euro").withValue(3.4).withYear(135).withId(1);
    }

    public static MuntBuilder anInvalidMuntWithNoName(){
        return aMunt().withName("").withCountry("Belgium").withCurrency("euro").withValue(3.2).withYear(1500);
    }

    public static MuntBuilder anInvalidMuntWithNoCountry(){
        return aMunt().withName("No Country").withCountry("").withCurrency("euro").withValue(3.2).withYear(1500);
    }

    public static MuntBuilder anInvalidMuntWithNoCurrency(){
        return aMunt().withName("No Currency").withCountry("Belgium").withValue(3.2).withYear(1500);
    }

    public static MuntBuilder anInvalidMuntWithNoValue(){
        return aMunt().withName("Novalue").withCountry("Belgium").withCurrency("euro").withYear(1500);
    }

    public static MuntBuilder anInvalidMuntWithValueLessThan0(){
        return aMunt().withName("Valuelessthanzero").withCountry("Belgium").withCurrency("euro").withValue(-1.0).withYear(1500);
    }

    public static MuntBuilder anInvalidMuntWithNoYear(){
        return aMunt().withName("Noyear").withCountry("Belgium").withCurrency("euro").withValue(3.2);
    }

    public static MuntBuilder anInvalidMuntWithYearGreaterThan2022(){
        return aMunt().withName("Yeargreaterthan2022").withCountry("Belgium").withCurrency("euro").withValue(3.2).withYear(2050);
    }

    public static MuntBuilder anInvalidMuntWithYearLessThan600BC(){
        return aMunt().withName("Yearlessthan600bc").withCountry("Belgium").withCurrency("euro").withValue(3.2).withYear(-700);
    }

    public Munt build(){
        Munt munt = new Munt();
        munt.setId(id);
        munt.setName(name);
        munt.setCountry(country);
        munt.setCurrency(currency);
        munt.setValue(value);
        munt.setYear(year);
        return munt;
    }
}
