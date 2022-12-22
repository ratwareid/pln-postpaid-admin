# pln-postpaid-admin
Springboot Web using JPA Postgresql and thmeleaf

Admin panel pembayaran PLN Postpaid.

<br>==========================================================================<br>

Dibuat menggunakan :
- Springboot
- Hibernate jpa
- Postgres
- Thymeleaf
- Lombok
- Swagger OpenAPI

<br>==========================================================================<br>

Feature :

Master Data :
- User : Administrator
- Pelanggan
- Tarif
- Level : Role akses

Transaksi :
- Penggunaan : mencatat penggunaan listrik pelanggan
- Tagihan
- Pembayaran

Laporan :
- Laporan tagihan dan pembayaran

<br>==========================================================================<br>

Panduan instalasi :

Software yang diperlukan :
- Postgres server versi 9.6 atau lebih baru
- PGAdmin 4
- Java SDK versi 11
- IDE Intellij ultimate atau sejenis

1. Persiapan Database :
- Download backup database di : <a href="https://github.com/ratwareid/pln-postpaid-admin/tree/master/database"> Klik Disini</a>
- Buka PGAdmin 4 lalu buat database baru (contoh : spring_test2)
- Restore database menggunakan file .backup yang telah didownload diatas

2. Build dan Jalankan program :
- Buka IDE Intellij laluopen project pln-postpaid-admin yang sudah kalian download.
- Download maven library
- Buka <a href="https://github.com/ratwareid/pln-postpaid-admin/blob/master/src/main/resources/application.properties">application.properties</a> lalu ubah datasource url, datasource name dan password disesuaikan dengan koneksi database kalian.
- Running spring application melalui IDE atau build dan running jar melalui terminal.

3. Buka url http://localhost:8081/login kemudian masukkan username : admin dan password: admin

<br>==========================================================================<br>

Preview Apps :

<img src="https://github.com/ratwareid/pln-postpaid-admin/blob/master/preview/loginpage.png" alt="Login Page" width="800" height="500">
<img src="https://github.com/ratwareid/pln-postpaid-admin/blob/master/preview/masterdata.png" alt="Master Data" width="800" height="500">
<img src="https://github.com/ratwareid/pln-postpaid-admin/blob/master/preview/transaksi.png" alt="Transaksi" width="800" height="500">
<img src="https://github.com/ratwareid/pln-postpaid-admin/blob/master/preview/laporan.png" alt="Laporan" width="800" height="500">


