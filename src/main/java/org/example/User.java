package org.example;
public class User {  // возможное добавление суммы у клиента
    private String fio;
    private int role;
    private int age;
    private String email;
    private char[] password;
    private String additionalInfo;

    public User(String fio, int role, int age, String email, char[] password, String additionalInfo) {
        this.fio = fio;
        this.role = role;
        this.age = age;
        this.email = email;
        this.password = password;
        this.additionalInfo = additionalInfo;
    }

    public User(String fio, int role, int age, String email, char[] password) {
        this.fio = fio;
        this.role = role;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public User(String email, char[] password) {
        this.email = email;
        this.password = password;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Full name: " + fio + " , Age: " + age + " , email: " + email;
    }
}
