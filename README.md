<img width="1920" height="1080" alt="- Cover - Anggota Kelompok - Latar Belakang   Identifikasi Masalah - Usulan Solusi   Relevansi terhadap SDGs - Tech Stack yang digunakan - Arsitektur Database (ERD Logical   Relational) - Use Case, Tabel Hak Akses, dan " src="https://github.com/user-attachments/assets/bfd8e60d-4aa9-46f3-a041-606b82b712ef" />

# PROJECT SPRING

## Sistem Informasi Manajemen Produksi dan Distribusi Air Minum Kemasan

## SDGs: Clean Water and Sanitation

## Fokus Area: Pengelolaan Informasi Produksi dan Distribusi Air Minum Kemasan

------------------------------------------------------------------------

### Anggota Kelompok

1.  Muhammad Davi Aditya Pratama (2509116070)
2.  Aulia Ashylla Ananda Putri Hariawan (2509116076)
3.  Muhammad Arza Dwiarto Anugerah (2509116007)
4.  Dzulrrahim Oscar Aditya Akbar (2509116107)

## Deskripsi Singkat Program

Project SPRING merupakan aplikasi berbasis Java untuk membantu proses
pengelolaan produksi dan distribusi air minum kemasan. Aplikasi ini
menggunakan Java Swing sebagai antarmuka dan MySQL sebagai basis data.

Sistem memiliki empat jenis pengguna, yaitu Admin, Petugas Produksi,
Petugas Gudang, dan Petugas Distribusi. Setiap pengguna memiliki hak
akses sesuai dengan tugasnya.

Aplikasi ini membantu pencatatan data produksi, produk, agen, dan
distribusi agar informasi operasional dapat dikelola dalam satu sistem.

------------------------------------------------------------------------

## FLOWCHART PROGRAM

### 1. Flowchart Login

![Flowchart Login](assets/flowchart/01-login.png)

### 2. Flowchart Menu Petugas Produksi dan Gudang

![Flowchart Menu Petugas Produksi dan
Gudang](assets/flowchart/02-menu-produksi-gudang.png)

### 3. Flowchart Menu Petugas Distribusi dan Admin

![Flowchart Menu Petugas Distribusi dan
Admin](assets/flowchart/03-menu-distribusi-admin.png)

### 4. Flowchart Pengelolaan Data Produksi

![Flowchart Pengelolaan Data
Produksi](assets/flowchart/04-crud-produksi.png)

### 5. Flowchart Pengelolaan Data Distribusi

![Flowchart Pengelolaan Data
Distribusi](assets/flowchart/05-crud-distribusi.png)

### 6. Flowchart Pengelolaan Data Agen

![Flowchart Pengelolaan Data Agen](assets/flowchart/06-crud-agen.png)

### 7. Flowchart Pengelolaan Data Produk

![Flowchart Pengelolaan Data
Produk](assets/flowchart/07-crud-produk.png)

### 8. Flowchart Pengelolaan Data Akun

![Flowchart Pengelolaan Data Akun](assets/flowchart/08-crud-akun.png)

------------------------------------------------------------------------

## USE CASE DIAGRAM

![Use Case Diagram Project SPRING](assets/use-case.png)

------------------------------------------------------------------------

## ENTITY RELATIONSHIP DIAGRAM (ERD)

### Logical ERD

![Logical ERD Project SPRING](assets/erd-logical.png)

### Relational ERD

![Relational ERD Project SPRING](assets/erd-relational.png)

------------------------------------------------------------------------

# Analisis Penerapan 4 Pilar OOP pada Project SPRING

Project SPRING dikembangkan menggunakan bahasa pemrograman Java dengan
menerapkan empat pilar Object-Oriented Programming (OOP), yaitu
Encapsulation, Inheritance, Abstraction, dan Polymorphism.

## 1. Encapsulation (Enkapsulasi)

Enkapsulasi digunakan untuk melindungi atribut dalam sebuah class. Pada
class `Petugas`, atribut dibuat menggunakan access modifier `private`.
Atribut tersebut diakses melalui getter dan setter.

### Bukti kode `Petugas.java`

![Atribut private pada class
Petugas](assets/oop/01-encapsulation-fields.png)

![Getter dan setter pada class
Petugas](assets/oop/02-encapsulation-getters-setters.png)

## 2. Inheritance (Pewarisan)

Inheritance memungkinkan class turunan menggunakan atribut dan method
dari class induk. Pada Project SPRING, `PetugasAdmin` merupakan turunan
dari class `Petugas` dengan menggunakan keyword `extends`.

### Bukti kode `PetugasAdmin.java`

![Inheritance PetugasAdmin extends
Petugas](assets/oop/03-inheritance-petugas-admin.png)

## 3. Abstraction (Abstraksi)

Abstraksi diterapkan menggunakan abstract class `Produk` dan interface
`CrudDAO<T>`. Class `Produk` memiliki method abstrak yang harus
diimplementasikan oleh class turunannya. Interface `CrudDAO<T>`
mendefinisikan method dasar untuk operasi data.

### Bukti kode `Produk.java` dan `CrudDAO.java`

![Abstract class Produk](assets/oop/04-abstraction-produk-class.png)

![Abstract methods pada
Produk](assets/oop/05-abstraction-abstract-methods.png)

![Interface CrudDAO](assets/oop/06-abstraction-cruddao-interface.png)

## 4. Polymorphism (Polimorfisme)

Polimorfisme diterapkan melalui method overriding dan overloading.

### a. Method Overriding

`ProdukBotol` dan `ProdukGalon` mengimplementasikan ulang method dari
class abstrak `Produk`. Nama method tetap sama, tetapi hasilnya
menyesuaikan jenis produk.

![Overriding pada
ProdukBotol](assets/oop/07-polymorphism-overriding-botol.png)

![Overriding pada
ProdukGalon](assets/oop/08-polymorphism-overriding-galon.png)

### b. Method Overloading

Method `cariAgen()` pada `AgenController` memiliki dua bentuk dengan
parameter berbeda. Method pertama mencari agen berdasarkan ID, sedangkan
method kedua mencari agen berdasarkan nama dan kecamatan.

![Overloading method
cariAgen](assets/oop/09-polymorphism-overloading-agen.png)

------------------------------------------------------------------------

## DATABASE

Project SPRING menggunakan MySQL dengan nama database `db_amdk_spring`.

File database: [`db_amdk_spring.sql`](database/db_amdk_spring.sql)

------------------------------------------------------------------------

<p align="center">
  **PROJECT SPRING**  
  Sistem Informasi Manajemen Produksi dan Distribusi Air Minum Kemasan  
  Program Studi Sistem Informasi · Universitas Mulawarman · 2026
</p>
