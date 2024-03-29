package com.project.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer absentee; // yoklama

    private Double midtermExam;

    private Double finalExam;

    private Double examAverage; //100 --> AA , 30 --> FF

    private String infoNote;

    @Enumerated(EnumType.STRING)
    private Note letterGrade; // AA , Aa , aa

    @ManyToOne // 1 ogretmen 1den fazla student info ya atanabilir.
    @JsonIgnore//toString ile tazdirilirken STACKOVERFLOW U ONLE
    private User teacher;

    @ManyToOne  // 1 ogrenci aldigi ders sayisi kadar studentInfosu vardir
    @JsonIgnore//toString ile tazdirilirken STACKOVERFLOW U ONLER
    private User student;

    @ManyToOne//iliskiyi sadece burdan setledigim icin Lesson tarafta iliski kurmadim, bu yuzden JsonIgnore da gerek yok loop a zaten girmeyecek yazdirirken
    private Lesson lesson;

    @OneToOne
    private EducationTerm educationTerm;


}