-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 08 Des 2024 pada 14.05
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pergudangan`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang_keluar`
--

CREATE TABLE `barang_keluar` (
  `id_penjualan` int(11) NOT NULL,
  `tanggal_keluar` date NOT NULL,
  `id_barang` varchar(50) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `pendapatan` decimal(10,2) NOT NULL,
  `id_customer` int(11) NOT NULL,
  `status` enum('Terkirim','Pending','Batal') NOT NULL DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `barang_keluar`
--

INSERT INTO `barang_keluar` (`id_penjualan`, `tanggal_keluar`, `id_barang`, `jumlah`, `pendapatan`, `id_customer`, `status`) VALUES
(1, '2023-12-01', 'ESP8266', 10, 600000.00, 1, 'Terkirim'),
(2, '2023-12-05', 'UNO', 5, 700000.00, 2, 'Pending'),
(3, '2023-12-10', 'RPI4', 15, 12900000.00, 3, 'Terkirim');

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang_masuk`
--

CREATE TABLE `barang_masuk` (
  `id_masuk` int(11) NOT NULL,
  `id_supplier` int(11) NOT NULL,
  `tanggal_masuk` date NOT NULL,
  `id_barang` varchar(11) NOT NULL,
  `nama_barang` varchar(200) NOT NULL,
  `kategori` varchar(50) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `harga_beli` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `barang_masuk`
--

INSERT INTO `barang_masuk` (`id_masuk`, `id_supplier`, `tanggal_masuk`, `id_barang`, `nama_barang`, `kategori`, `jumlah`, `harga_beli`) VALUES
(1, 1, '2023-08-01', 'ESP8266', 'ESP8266 WiFi Module', 'Komponen Jaringan', 50, 2000000.00),
(2, 1, '2023-11-05', 'UNO', 'Arduino Uno R3', 'Mikrokontroler', 30, 3600000.00),
(3, 1, '2023-11-10', 'RPI4', 'Raspberry Pi 4 Model B', 'Komputer Mini', 20, 16000000.00),
(4, 1, '2023-11-12', 'DS18B20', 'Sensor Suhu DS18B20', 'Sensor', 100, 1500000.00),
(5, 3, '2023-11-15', 'MQ135', 'Sensor Gas MQ135', 'Sensor', 60, 1500000.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `customer`
--

CREATE TABLE `customer` (
  `id_customer` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `kontak` varchar(50) NOT NULL,
  `alamat` text NOT NULL,
  `tanggal_bergabung` date NOT NULL DEFAULT curdate(),
  `jenis_customer` enum('Individu','Perusahaan','Reseller') NOT NULL,
  `status` enum('Aktif','Tidak Aktif') NOT NULL DEFAULT 'Aktif'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `customer`
--

INSERT INTO `customer` (`id_customer`, `nama`, `kontak`, `alamat`, `tanggal_bergabung`, `jenis_customer`, `status`) VALUES
(1, 'John Doe', '081122334455', 'Jl. Mangga No. 5, Jakarta', '2023-01-15', 'Individu', 'Aktif'),
(2, 'Toko Sukses', '082233445566', 'Jl. Apel No. 3, Bandung', '2023-05-20', 'Reseller', 'Aktif'),
(3, 'CV. Makmur', '083344556677', 'Jl. Durian No. 8, Surabaya', '2023-07-25', 'Individu', 'Aktif');

-- --------------------------------------------------------

--
-- Struktur dari tabel `evaluasisupplier`
--

CREATE TABLE `evaluasisupplier` (
  `id_evaluasi` int(11) NOT NULL,
  `id_supplier` int(11) DEFAULT NULL,
  `kualitas_barang` int(11) DEFAULT NULL,
  `ketepatan_pengiriman` int(11) DEFAULT NULL,
  `pelayanan` int(11) DEFAULT NULL,
  `komentar` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `evaluasisupplier`
--

INSERT INTO `evaluasisupplier` (`id_evaluasi`, `id_supplier`, `kualitas_barang`, `ketepatan_pengiriman`, `pelayanan`, `komentar`) VALUES
(1, 1, 4, 3, 4, 'Pengiriman delay');

-- --------------------------------------------------------

--
-- Struktur dari tabel `hargajual`
--

CREATE TABLE `hargajual` (
  `id_harga` int(11) NOT NULL,
  `id_barang` varchar(50) NOT NULL,
  `hargaunit` decimal(10,2) NOT NULL,
  `hargagrosir` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `hargajual`
--

INSERT INTO `hargajual` (`id_harga`, `id_barang`, `hargaunit`, `hargagrosir`) VALUES
(1, 'ESP8266', 60000.00, 55000.00),
(2, 'UNO', 140000.00, 130000.00),
(3, 'RPI4', 860000.00, 840000.00),
(4, 'DS18B20', 20000.00, 18000.00),
(5, 'MQ135', 30000.00, 27000.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `laporan_barang`
--

CREATE TABLE `laporan_barang` (
  `id_laporan` int(11) NOT NULL,
  `id_barang` varchar(50) NOT NULL,
  `deskripsi` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `laporan_barang`
--

INSERT INTO `laporan_barang` (`id_laporan`, `id_barang`, `deskripsi`) VALUES
(1, 'DS18B20', 'Kondisi sedikit cacat');

-- --------------------------------------------------------

--
-- Struktur dari tabel `stokbarang`
--

CREATE TABLE `stokbarang` (
  `id_stok` int(11) NOT NULL,
  `id_barang` varchar(50) NOT NULL,
  `nama_barang` varchar(255) NOT NULL,
  `jumlah` int(11) NOT NULL DEFAULT 0,
  `stok_keluar` int(11) NOT NULL DEFAULT 0,
  `stok_sisa` int(11) GENERATED ALWAYS AS (`jumlah` - `stok_keluar`) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `stokbarang`
--

INSERT INTO `stokbarang` (`id_stok`, `id_barang`, `nama_barang`, `jumlah`, `stok_keluar`) VALUES
(1, 'DS18B20', 'Sensor Suhu DS18B20', 100, 0),
(2, 'ESP8266', 'ESP8266 WiFi Module', 50, 10),
(3, 'MQ135', 'Sensor Gas MQ135', 60, 0),
(4, 'RPI4', 'Raspberry Pi 4 Model B', 20, 15),
(5, 'UNO', 'Arduino Uno R3', 30, 5);

-- --------------------------------------------------------

--
-- Struktur dari tabel `supplier`
--

CREATE TABLE `supplier` (
  `id_supplier` int(11) NOT NULL,
  `nama_supplier` varchar(100) NOT NULL,
  `kontak` varchar(20) NOT NULL,
  `alamat` text NOT NULL,
  `email` varchar(100) NOT NULL,
  `status` enum('aktif','tidak aktif') NOT NULL DEFAULT 'aktif'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `supplier`
--

INSERT INTO `supplier` (`id_supplier`, `nama_supplier`, `kontak`, `alamat`, `email`, `status`) VALUES
(1, 'PT. Maju Jaya', '081234567890', 'Jl. Kebon Jeruk No. 10, Jakarta', 'supplier@majujaya.com', 'aktif'),
(2, 'CV. Sejahtera', '082345678901', 'Jl. Melati No. 20, Bandung', 'supplier@sejahtera.com', 'aktif'),
(3, 'UD. Abadi', '083456789012', 'Jl. Mawar No. 15, Surabaya', 'supplier@abadi.com', 'aktif');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `role`) VALUES
(1, 'admin@gmail.com', 'admin123', 'admin'),
(2, 'manager@gmail.com', 'manager123', 'manager'),
(3, 'staff@gmail.com', 'staff123', 'staff');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `barang_keluar`
--
ALTER TABLE `barang_keluar`
  ADD PRIMARY KEY (`id_penjualan`),
  ADD KEY `id_barang` (`id_barang`),
  ADD KEY `fk_customer_barang_keluar` (`id_customer`);

--
-- Indeks untuk tabel `barang_masuk`
--
ALTER TABLE `barang_masuk`
  ADD PRIMARY KEY (`id_masuk`),
  ADD KEY `id_supplier` (`id_supplier`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indeks untuk tabel `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id_customer`);

--
-- Indeks untuk tabel `evaluasisupplier`
--
ALTER TABLE `evaluasisupplier`
  ADD PRIMARY KEY (`id_evaluasi`),
  ADD KEY `id_supplier` (`id_supplier`);

--
-- Indeks untuk tabel `hargajual`
--
ALTER TABLE `hargajual`
  ADD PRIMARY KEY (`id_harga`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indeks untuk tabel `laporan_barang`
--
ALTER TABLE `laporan_barang`
  ADD PRIMARY KEY (`id_laporan`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indeks untuk tabel `stokbarang`
--
ALTER TABLE `stokbarang`
  ADD PRIMARY KEY (`id_stok`),
  ADD UNIQUE KEY `id_barang` (`id_barang`);

--
-- Indeks untuk tabel `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`id_supplier`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `barang_keluar`
--
ALTER TABLE `barang_keluar`
  MODIFY `id_penjualan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `barang_masuk`
--
ALTER TABLE `barang_masuk`
  MODIFY `id_masuk` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `customer`
--
ALTER TABLE `customer`
  MODIFY `id_customer` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `evaluasisupplier`
--
ALTER TABLE `evaluasisupplier`
  MODIFY `id_evaluasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `hargajual`
--
ALTER TABLE `hargajual`
  MODIFY `id_harga` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `laporan_barang`
--
ALTER TABLE `laporan_barang`
  MODIFY `id_laporan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `stokbarang`
--
ALTER TABLE `stokbarang`
  MODIFY `id_stok` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT untuk tabel `supplier`
--
ALTER TABLE `supplier`
  MODIFY `id_supplier` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `barang_keluar`
--
ALTER TABLE `barang_keluar`
  ADD CONSTRAINT `barang_keluar_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang_masuk` (`id_barang`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_customer_barang_keluar` FOREIGN KEY (`id_customer`) REFERENCES `customer` (`id_customer`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `barang_masuk`
--
ALTER TABLE `barang_masuk`
  ADD CONSTRAINT `barang_masuk_ibfk_1` FOREIGN KEY (`id_supplier`) REFERENCES `supplier` (`id_supplier`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `evaluasisupplier`
--
ALTER TABLE `evaluasisupplier`
  ADD CONSTRAINT `evaluasisupplier_ibfk_1` FOREIGN KEY (`id_supplier`) REFERENCES `supplier` (`id_supplier`);

--
-- Ketidakleluasaan untuk tabel `hargajual`
--
ALTER TABLE `hargajual`
  ADD CONSTRAINT `hargajual_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang_masuk` (`id_barang`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `laporan_barang`
--
ALTER TABLE `laporan_barang`
  ADD CONSTRAINT `laporan_barang_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang_masuk` (`id_barang`);

--
-- Ketidakleluasaan untuk tabel `stokbarang`
--
ALTER TABLE `stokbarang`
  ADD CONSTRAINT `stokbarang_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang_masuk` (`id_barang`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
