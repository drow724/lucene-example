package com.example.demo.entity;

import org.apache.lucene.document.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Member {

    @Id
    private Long id;

    private String name;

    private String birth;

    private String city;

    private String gender;

    private LocalDate joinDt;

    private String mail;

    private String loginIp;

    public Member() {
    }

    public Member(Document document) {
        this.id = Long.parseLong(document.get("id"));
        this.name = document.get("name");
        this.birth = document.get("birth");
        this.city = document.get("city");
        this.gender = document.get("gender");

        if(document.get("joinDt") != null && document.get("joinDt").length() >= 10) {
            String year = document.get("joinDt").substring(0, 4);
            String month = document.get("joinDt").substring(5, 7);
            String day = document.get("joinDt").substring(8, 10);

            this.joinDt = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        }

        this.mail = document.get("mail");
        this.loginIp = document.get("loginIp");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

    public String getCity() {
        return city;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getJoinDt() {
        return joinDt;
    }

    public String getMail() {
        return mail;
    }

    public String getLoginIp() {
        return loginIp;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birth='" + birth + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                ", joinDt=" + joinDt +
                ", mail='" + mail + '\'' +
                ", loginIp='" + loginIp + '\'' +
                '}';
    }
}
