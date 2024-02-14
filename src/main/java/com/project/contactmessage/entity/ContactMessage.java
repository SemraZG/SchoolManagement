package com.project.contactmessage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
/*
 	 Nesnelerimiz Ag ortaminda degerleri ile berbaer gonderilecekse bu yapi kullanlir
 ama RESTful API'lar genellikle JSON veya XML formatında veri
 alışverişi yapar. Bu tür serileştirme işlemleri, Java'nın yerleşik Serializable
 arayüzünden bağımsız olarak çalışır. Spring Boot gibi modern frameworkler, Jackson
 veya Gson gibi kütüphaneleri kullanarak nesneleri JSON'a otomatik olarak serileştirir
 ve deserializasyon yapar. Bu süreç, Java'nın yerel serileştirme mekanizmasından
 farklıdır. RESTful servislerde genellikle nesnelerin durumları JSON olarak iletilir
 ve bu süreçte Java'nın yerel serileştirme mekanizmasına ihtiyaç duyulmaz.
 */ //!!! implements SERIALIZABLE ACIKLAMA

@Data//Data kullanmak cok tavsiye dilmez,gettr settr toString Data'nin icinde gelir, EqualsHaashode dan dolayi cok tavsiye edilmez
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)//farkli opsiyonlarla constrctr olusturur

@Entity
public class ContactMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //normalde contactId daha dogru

    @NotNull
    private String name;

    @NotNull
    private String email;//requestleri dto class ile karsilayacgim unique olma duurmunu orda kontrol etmemiz daha mantikli

    @NotNull
    private String subject;

    @NotNull
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "US")
    private LocalDateTime dateTime;
}
