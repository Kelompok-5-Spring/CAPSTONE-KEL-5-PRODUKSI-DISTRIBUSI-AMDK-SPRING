-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 03, 2026 at 10:41 AM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_amdk_spring`
--

-- --------------------------------------------------------

--
-- Table structure for table `agen`
--

CREATE TABLE `agen` (
  `id_agen` varchar(10) NOT NULL,
  `nama_agen` varchar(100) NOT NULL,
  `no_telepon` varchar(15) NOT NULL,
  `alamat_lengkap` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `agen`
--

INSERT INTO `agen` (`id_agen`, `nama_agen`, `no_telepon`, `alamat_lengkap`) VALUES
('AGN001', 'Toko Segar Jaya', '081234567801', 'Jl. Trikora No. 12, Kec. Palaran, Samarinda'),
('AGN002', 'Depot Air Minum Barokah', '081234567802', 'Jl. Sultan Suriansyah No. 45, Kec. Samarinda Ilir, Samarinda'),
('AGN003', 'Toko Sembako Berkah', '081234567803', 'Jl. Ahmad Yani No. 88, Kec. Samarinda Kota, Samarinda'),
('AGN004', 'Minimarket Sentrosa', '081234567804', 'Jl. D.I. Panjaitan No. 101, Kec. Samarinda Utara, Samarinda'),
('AGN005', 'Toko Tirta Abadi', '081234567805', 'Jl. M. Yamin No. 23, Kec. Samarinda Ulu, Samarinda'),
('AGN006', 'Depot Sumber Rejeki', '081234567806', 'Jl. Untung Suropati No. 56, Kec. Sungai Kunjang, Samarinda'),
('AGN007', 'Toko Maju Bersama', '081234567807', 'Jl. Gotong Royong No. 15, Kec. Palaran, Samarinda'),
('AGN008', 'Minimarket Melati', '081234567808', 'Jl. Jelawat No. 77, Kec. Samarinda Ilir, Samarinda'),
('AGN009', 'Toko Kelontong Utama', '081234567809', 'Jl. Mulawarman No. 34, Kec. Samarinda Kota, Samarinda'),
('AGN010', 'Depot Minum Sehat', '081234567810', 'Jl. PM. Noor No. 90, Kec. Samarinda Utara, Samarinda'),
('AGN011', 'Toko Kasih Ibu', '081234567811', 'Jl. Juanda No. 112, Kec. Samarinda Ulu, Samarinda'),
('AGN012', 'Minimarket Berkat Jaya', '081234567812', 'Jl. Jakarta No. 40, Kec. Sungai Kunjang, Samarinda'),
('AGN013', 'Depot Tirta Bening', '081234567813', 'Jl. Pelabuhan No. 05, Kec. Samarinda Kota, Samarinda'),
('AGN014', 'Toko Makmur Sejahtera', '081234567814', 'Jl. Kadrie Oening No. 67, Kec. Samarinda Ulu, Samarinda'),
('AGN015', 'Toko Baru Palaran', '081234567815', 'Jl. Masjidan No. 19, Kec. Palaran, Samarinda');

-- --------------------------------------------------------

--
-- Table structure for table `detail_distribusi`
--

CREATE TABLE `detail_distribusi` (
  `id_detail_distribusi` varchar(10) NOT NULL,
  `id_agen` varchar(10) NOT NULL,
  `id_distribusi` varchar(10) NOT NULL,
  `jenis_produk` enum('BOTOL_600ML','BOTOL_1500ML','GALON_5L','GALON_19L') NOT NULL,
  `jumlah_pesan` int NOT NULL,
  `tanggal_pesan` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `detail_distribusi`
--

INSERT INTO `detail_distribusi` (`id_detail_distribusi`, `id_agen`, `id_distribusi`, `jenis_produk`, `jumlah_pesan`, `tanggal_pesan`) VALUES
('DTL001', 'AGN001', 'DST001', 'BOTOL_600ML', 10, '2026-10-01'),
('DTL002', 'AGN001', 'DST001', 'BOTOL_1500ML', 5, '2026-10-01'),
('DTL003', 'AGN007', 'DST001', 'GALON_19L', 3, '2026-10-01'),
('DTL004', 'AGN002', 'DST002', 'BOTOL_1500ML', 5, '2026-10-02'),
('DTL005', 'AGN008', 'DST002', 'BOTOL_600ML', 12, '2026-10-02'),
('DTL006', 'AGN003', 'DST003', 'GALON_19L', 5, '2026-10-03'),
('DTL007', 'AGN009', 'DST003', 'GALON_5L', 8, '2026-10-03'),
('DTL008', 'AGN004', 'DST004', 'BOTOL_600ML', 10, '2026-10-04'),
('DTL009', 'AGN010', 'DST004', 'BOTOL_1500ML', 10, '2026-10-04'),
('DTL010', 'AGN005', 'DST005', 'GALON_5L', 4, '2026-10-05'),
('DTL011', 'AGN011', 'DST005', 'GALON_19L', 6, '2026-10-05'),
('DTL012', 'AGN006', 'DST006', 'BOTOL_600ML', 15, '2026-10-06'),
('DTL013', 'AGN012', 'DST006', 'BOTOL_1500ML', 10, '2026-10-06'),
('DTL014', 'AGN013', 'DST007', 'BOTOL_600ML', 20, '2026-10-07'),
('DTL015', 'AGN014', 'DST008', 'GALON_19L', 10, '2026-10-08'),
('DTL016', 'AGN015', 'DST009', 'BOTOL_1500ML', 12, '2026-10-09'),
('DTL017', 'AGN010', 'DST010', 'GALON_5L', 5, '2026-10-10'),
('DTL018', 'AGN004', 'DST011', 'BOTOL_600ML', 8, '2026-10-11'),
('DTL019', 'AGN009', 'DST012', 'GALON_19L', 11, '2026-10-12'),
('DTL020', 'AGN001', 'DST013', 'BOTOL_600ML', 10, '2026-10-13'),
('DTL021', 'AGN002', 'DST014', 'BOTOL_1500ML', 14, '2026-10-14'),
('DTL022', 'AGN007', 'DST015', 'GALON_5L', 5, '2026-10-15');

-- --------------------------------------------------------

--
-- Table structure for table `distribusi`
--

CREATE TABLE `distribusi` (
  `id_distribusi` varchar(10) NOT NULL,
  `tanggal_kirim` date NOT NULL,
  `status_kirim` enum('Terkirim','Tidak Terkirim','Dalam Pengiriman') NOT NULL,
  `Petugas_Gudang_id_petugas` varchar(10) NOT NULL,
  `Petugas_Distribusi_id_petugas` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `distribusi`
--

INSERT INTO `distribusi` (`id_distribusi`, `tanggal_kirim`, `status_kirim`, `Petugas_Gudang_id_petugas`, `Petugas_Distribusi_id_petugas`) VALUES
('DST001', '2026-10-02', 'Terkirim', 'PTG004', 'PTG006'),
('DST002', '2026-10-03', 'Terkirim', 'PTG004', 'PTG007'),
('DST003', '2026-10-04', 'Terkirim', 'PTG004', 'PTG008'),
('DST004', '2026-10-05', 'Terkirim', 'PTG004', 'PTG009'),
('DST005', '2026-10-06', 'Terkirim', 'PTG004', 'PTG017'),
('DST006', '2026-10-07', 'Terkirim', 'PTG004', 'PTG018'),
('DST007', '2026-10-08', 'Terkirim', 'PTG004', 'PTG008'),
('DST008', '2026-10-09', 'Terkirim', 'PTG004', 'PTG017'),
('DST009', '2026-10-10', 'Terkirim', 'PTG004', 'PTG006'),
('DST010', '2026-10-11', 'Terkirim', 'PTG004', 'PTG009'),
('DST011', '2026-10-12', 'Terkirim', 'PTG004', 'PTG009'),
('DST012', '2026-10-13', 'Terkirim', 'PTG004', 'PTG008'),
('DST013', '2026-10-14', 'Terkirim', 'PTG004', 'PTG006'),
('DST014', '2026-10-15', 'Terkirim', 'PTG004', 'PTG007'),
('DST015', '2026-10-16', 'Terkirim', 'PTG004', 'PTG006');

-- --------------------------------------------------------

--
-- Table structure for table `petugas`
--

CREATE TABLE `petugas` (
  `id_petugas` varchar(10) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('Admin','Produksi','Gudang','Distribusi') NOT NULL,
  `nama` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `petugas`
--

INSERT INTO `petugas` (`id_petugas`, `username`, `password`, `role`, `nama`) VALUES
('PTG001', 'admin_utama', 'pass_hash_01', 'Admin', 'Ahmad Ridwan'),
('PTG002', 'admin_keuangan', 'pass_hash_02', 'Admin', 'Siti Rahmawati'),
('PTG003', 'gudang_inbound', 'pass_hash_03', 'Gudang', 'Budi Santoso'),
('PTG004', 'gudang_outbound', 'pass_hash_04', 'Gudang', 'Rian Hidayat'),
('PTG005', 'gudang_checker', 'pass_hash_05', 'Gudang', 'Dina Lestari'),
('PTG006', 'dist_palaran', 'pass_hash_06', 'Distribusi', 'Donny Kurniawan'),
('PTG007', 'dist_ilir', 'pass_hash_07', 'Distribusi', 'Eka Pratama'),
('PTG008', 'dist_kota', 'pass_hash_08', 'Distribusi', 'Fajar Ramadhan'),
('PTG009', 'dist_utara', 'pass_hash_09', 'Distribusi', 'Gilang Pratama'),
('PTG010', 'prod_pengisian1', 'pass_hash_10', 'Produksi', 'Hendra Wijaya'),
('PTG011', 'prod_kemas1', 'pass_hash_11', 'Produksi', 'Indah Permata'),
('PTG012', 'prod_qc1', 'pass_hash_12', 'Produksi', 'Joko Susilo'),
('PTG013', 'prod_pengisian2', 'pass_hash_13', 'Produksi', 'Kiki Amelia'),
('PTG014', 'prod_kemas2', 'pass_hash_14', 'Produksi', 'Lukman Hakim'),
('PTG015', 'prod_qc2', 'pass_hash_15', 'Produksi', 'Mawar Eva'),
('PTG016', 'prod_pengisian3', 'pass_hash_16', 'Produksi', 'Nanda Putra'),
('PTG017', 'dist_ulu', 'pass_hash_17', 'Distribusi', 'Rizal Permana'),
('PTG018', 'dist_kunjang', 'pass_hash_18', 'Distribusi', 'Bagas Saputra');

-- --------------------------------------------------------

--
-- Table structure for table `petugas_admin`
--

CREATE TABLE `petugas_admin` (
  `id_petugas` varchar(10) NOT NULL,
  `spesialisasi_admin` enum('Pengawas','Manajemen') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `petugas_admin`
--

INSERT INTO `petugas_admin` (`id_petugas`, `spesialisasi_admin`) VALUES
('PTG001', 'Pengawas'),
('PTG002', 'Manajemen');

-- --------------------------------------------------------

--
-- Table structure for table `petugas_distribusi`
--

CREATE TABLE `petugas_distribusi` (
  `id_petugas` varchar(10) NOT NULL,
  `wilayah_tugas` enum('Palaran','Samarinda Ilir','Samarinda Kota','Samarinda Utara','Samarinda Ulu','Sungai Kunjang') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `petugas_distribusi`
--

INSERT INTO `petugas_distribusi` (`id_petugas`, `wilayah_tugas`) VALUES
('PTG006', 'Palaran'),
('PTG007', 'Samarinda Ilir'),
('PTG008', 'Samarinda Kota'),
('PTG009', 'Samarinda Utara'),
('PTG017', 'Samarinda Ulu'),
('PTG018', 'Sungai Kunjang');

-- --------------------------------------------------------

--
-- Table structure for table `petugas_gudang`
--

CREATE TABLE `petugas_gudang` (
  `id_petugas` varchar(10) NOT NULL,
  `spesialisasi_tugas` enum('Inbound','Outbound','Stock Checker') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `petugas_gudang`
--

INSERT INTO `petugas_gudang` (`id_petugas`, `spesialisasi_tugas`) VALUES
('PTG003', 'Inbound'),
('PTG004', 'Outbound'),
('PTG005', 'Stock Checker');

-- --------------------------------------------------------

--
-- Table structure for table `petugas_produksi`
--

CREATE TABLE `petugas_produksi` (
  `id_petugas` varchar(10) NOT NULL,
  `spesialisasi_proses` enum('Pengisian','Pengemasan','Quality Check') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `petugas_produksi`
--

INSERT INTO `petugas_produksi` (`id_petugas`, `spesialisasi_proses`) VALUES
('PTG010', 'Pengisian'),
('PTG011', 'Pengemasan'),
('PTG012', 'Quality Check'),
('PTG013', 'Pengisian'),
('PTG014', 'Pengemasan'),
('PTG015', 'Quality Check'),
('PTG016', 'Pengisian');

-- --------------------------------------------------------

--
-- Table structure for table `produk`
--

CREATE TABLE `produk` (
  `id_produk` varchar(15) NOT NULL,
  `nama_produk` varchar(100) NOT NULL,
  `harga_satuan` decimal(12,2) NOT NULL,
  `Produksi_id_produksi` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `produk`
--

INSERT INTO `produk` (`id_produk`, `nama_produk`, `harga_satuan`, `Produksi_id_produksi`) VALUES
('PDK001', 'Spring Botol 600ml', '45000.00', 'PRD001'),
('PDK002', 'Spring Botol 600ml', '45000.00', 'PRD001'),
('PDK003', 'Spring Botol 600ml', '45000.00', 'PRD001'),
('PDK004', 'Spring Botol 600ml', '45000.00', 'PRD001'),
('PDK005', 'Spring Botol 600ml', '45000.00', 'PRD001'),
('PDK006', 'Spring Botol 1500ml', '55000.00', 'PRD002'),
('PDK007', 'Spring Botol 1500ml', '55000.00', 'PRD002'),
('PDK008', 'Spring Botol 1500ml', '55000.00', 'PRD002'),
('PDK009', 'Spring Botol 1500ml', '55000.00', 'PRD002'),
('PDK010', 'Spring Botol 1500ml', '55000.00', 'PRD002'),
('PDK011', 'Spring Galon 19L', '18000.00', 'PRD003'),
('PDK012', 'Spring Galon 19L', '18000.00', 'PRD003'),
('PDK013', 'Spring Galon 19L', '18000.00', 'PRD003'),
('PDK014', 'Spring Galon 19L', '18000.00', 'PRD003'),
('PDK015', 'Spring Galon 19L', '18000.00', 'PRD003'),
('PDK016', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK017', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK018', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK019', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK020', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK021', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK022', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK023', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK024', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK025', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK026', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK027', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK028', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK029', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK030', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK031', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK032', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK033', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK034', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK035', 'Spring Botol 600ml', '45000.00', 'PRD004'),
('PDK036', 'Spring Botol 1500ml', '55000.00', 'PRD005'),
('PDK037', 'Spring Botol 1500ml', '55000.00', 'PRD005'),
('PDK038', 'Spring Botol 1500ml', '55000.00', 'PRD005'),
('PDK039', 'Spring Botol 1500ml', '55000.00', 'PRD005'),
('PDK040', 'Spring Botol 1500ml', '55000.00', 'PRD005'),
('PDK041', 'Spring Botol 1500ml', '55000.00', 'PRD005'),
('PDK042', 'Spring Botol 1500ml', '55000.00', 'PRD005'),
('PDK043', 'Spring Botol 1500ml', '55000.00', 'PRD005'),
('PDK044', 'Spring Botol 1500ml', '55000.00', 'PRD005'),
('PDK045', 'Spring Botol 1500ml', '55000.00', 'PRD005'),
('PDK046', 'Spring Galon 19L', '18000.00', 'PRD006'),
('PDK047', 'Spring Galon 19L', '18000.00', 'PRD006'),
('PDK048', 'Spring Galon 19L', '18000.00', 'PRD006'),
('PDK049', 'Spring Galon 19L', '18000.00', 'PRD006'),
('PDK050', 'Spring Galon 19L', '18000.00', 'PRD006'),
('PDK051', 'Spring Galon 19L', '18000.00', 'PRD006'),
('PDK052', 'Spring Galon 19L', '18000.00', 'PRD006'),
('PDK053', 'Spring Galon 19L', '18000.00', 'PRD006'),
('PDK054', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK055', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK056', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK057', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK058', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK059', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK060', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK061', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK062', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK063', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK064', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK065', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK066', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK067', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK068', 'Spring Botol 600ml', '45000.00', 'PRD007'),
('PDK069', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK070', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK071', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK072', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK073', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK074', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK075', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK076', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK077', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK078', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK079', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK080', 'Spring Botol 1500ml', '55000.00', 'PRD008'),
('PDK081', 'Spring Galon 19L', '18000.00', 'PRD009'),
('PDK082', 'Spring Galon 19L', '18000.00', 'PRD009'),
('PDK083', 'Spring Galon 19L', '18000.00', 'PRD009'),
('PDK084', 'Spring Galon 19L', '18000.00', 'PRD009'),
('PDK085', 'Spring Galon 19L', '18000.00', 'PRD009'),
('PDK086', 'Spring Galon 19L', '18000.00', 'PRD009'),
('PDK087', 'Spring Galon 19L', '18000.00', 'PRD009'),
('PDK088', 'Spring Galon 19L', '18000.00', 'PRD009'),
('PDK089', 'Spring Galon 19L', '18000.00', 'PRD009'),
('PDK090', 'Spring Galon 19L', '18000.00', 'PRD009'),
('PDK091', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK092', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK093', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK094', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK095', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK096', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK097', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK098', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK099', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK100', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK101', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK102', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK103', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK104', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK105', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK106', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK107', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK108', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK109', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK110', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK111', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK112', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK113', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK114', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK115', 'Spring Botol 600ml', '45000.00', 'PRD010'),
('PDK116', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK117', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK118', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK119', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK120', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK121', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK122', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK123', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK124', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK125', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK126', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK127', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK128', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK129', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK130', 'Spring Botol 1500ml', '55000.00', 'PRD011'),
('PDK131', 'Spring Galon 19L', '18000.00', 'PRD012'),
('PDK132', 'Spring Galon 19L', '18000.00', 'PRD012'),
('PDK133', 'Spring Galon 19L', '18000.00', 'PRD012'),
('PDK134', 'Spring Galon 19L', '18000.00', 'PRD012'),
('PDK135', 'Spring Galon 19L', '18000.00', 'PRD012'),
('PDK136', 'Spring Galon 19L', '18000.00', 'PRD012'),
('PDK137', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK138', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK139', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK140', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK141', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK142', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK143', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK144', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK145', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK146', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK147', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK148', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK149', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK150', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK151', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK152', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK153', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK154', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK155', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK156', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK157', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK158', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK159', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK160', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK161', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK162', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK163', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK164', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK165', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK166', 'Spring Botol 600ml', '45000.00', 'PRD013'),
('PDK167', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK168', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK169', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK170', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK171', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK172', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK173', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK174', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK175', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK176', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK177', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK178', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK179', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK180', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK181', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK182', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK183', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK184', 'Spring Botol 1500ml', '55000.00', 'PRD014'),
('PDK185', 'Spring Galon 19L', '18000.00', 'PRD015'),
('PDK186', 'Spring Galon 19L', '18000.00', 'PRD015'),
('PDK187', 'Spring Galon 19L', '18000.00', 'PRD015'),
('PDK188', 'Spring Galon 19L', '18000.00', 'PRD015'),
('PDK189', 'Spring Galon 19L', '18000.00', 'PRD015'),
('PDK190', 'Spring Galon 19L', '18000.00', 'PRD015'),
('PDK191', 'Spring Galon 19L', '18000.00', 'PRD015'),
('PDK192', 'Spring Galon 19L', '18000.00', 'PRD015'),
('PDK193', 'Spring Galon 19L', '18000.00', 'PRD015'),
('PDK194', 'Spring Galon 19L', '18000.00', 'PRD015'),
('PDK195', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK196', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK197', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK198', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK199', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK200', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK201', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK202', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK203', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK204', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK205', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK206', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK207', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK208', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK209', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK210', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK211', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK212', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK213', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK214', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK215', 'Spring Galon 5L', '12000.00', 'PRD016'),
('PDK216', 'Spring Galon 5L', '12000.00', 'PRD016');

-- --------------------------------------------------------

--
-- Table structure for table `produksi`
--

CREATE TABLE `produksi` (
  `id_produksi` varchar(10) NOT NULL,
  `jenis_produksi` varchar(50) NOT NULL,
  `tanggal_produksi` date NOT NULL,
  `status_produksi` enum('Direncanakan','Diproses','Selesai','Dibatalkan') NOT NULL,
  `jumlah_produksi` int NOT NULL,
  `Petugas_Produksi_id_petugas` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `produksi`
--

INSERT INTO `produksi` (`id_produksi`, `jenis_produksi`, `tanggal_produksi`, `status_produksi`, `jumlah_produksi`, `Petugas_Produksi_id_petugas`) VALUES
('PRD001', 'Spring Botol 600ml', '2026-10-01', 'Selesai', 5, 'PTG010'),
('PRD002', 'Spring Botol 1500ml', '2026-10-01', 'Selesai', 5, 'PTG011'),
('PRD003', 'Spring Galon 19L', '2026-10-01', 'Selesai', 5, 'PTG012'),
('PRD004', 'Spring Botol 600ml', '2026-10-02', 'Selesai', 20, 'PTG010'),
('PRD005', 'Spring Botol 1500ml', '2026-10-02', 'Selesai', 10, 'PTG011'),
('PRD006', 'Spring Galon 19L', '2026-10-03', 'Selesai', 8, 'PTG012'),
('PRD007', 'Spring Botol 600ml', '2026-10-04', 'Selesai', 15, 'PTG013'),
('PRD008', 'Spring Botol 1500ml', '2026-10-04', 'Selesai', 12, 'PTG014'),
('PRD009', 'Spring Galon 19L', '2026-10-05', 'Selesai', 10, 'PTG015'),
('PRD010', 'Spring Botol 600ml', '2026-10-06', 'Selesai', 25, 'PTG016'),
('PRD011', 'Spring Botol 1500ml', '2026-10-06', 'Selesai', 15, 'PTG010'),
('PRD012', 'Spring Galon 19L', '2026-10-08', 'Selesai', 6, 'PTG011'),
('PRD013', 'Spring Botol 600ml', '2026-10-07', 'Selesai', 30, 'PTG013'),
('PRD014', 'Spring Botol 1500ml', '2026-10-14', 'Selesai', 18, 'PTG014'),
('PRD015', 'Spring Galon 19L', '2026-10-12', 'Selesai', 10, 'PTG015'),
('PRD016', 'Spring Galon 5L', '2026-10-03', 'Selesai', 22, 'PTG016');

-- --------------------------------------------------------

--
-- Table structure for table `produk_botol`
--

CREATE TABLE `produk_botol` (
  `id_produk` varchar(15) NOT NULL,
  `volume_ml` decimal(12,2) NOT NULL,
  `pcs_per_dus` int NOT NULL,
  `id_distribusi` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `produk_botol`
--

INSERT INTO `produk_botol` (`id_produk`, `volume_ml`, `pcs_per_dus`, `id_distribusi`) VALUES
('PDK001', '600.00', 24, 'DST001'),
('PDK002', '600.00', 24, 'DST001'),
('PDK003', '600.00', 24, 'DST001'),
('PDK004', '600.00', 24, 'DST001'),
('PDK005', '600.00', 24, 'DST001'),
('PDK006', '1500.00', 12, 'DST001'),
('PDK007', '1500.00', 12, 'DST001'),
('PDK008', '1500.00', 12, 'DST001'),
('PDK009', '1500.00', 12, 'DST001'),
('PDK010', '1500.00', 12, 'DST001'),
('PDK016', '600.00', 24, 'DST001'),
('PDK017', '600.00', 24, 'DST001'),
('PDK018', '600.00', 24, 'DST001'),
('PDK019', '600.00', 24, 'DST001'),
('PDK020', '600.00', 24, 'DST001'),
('PDK021', '600.00', 24, 'DST002'),
('PDK022', '600.00', 24, 'DST002'),
('PDK023', '600.00', 24, 'DST002'),
('PDK024', '600.00', 24, 'DST002'),
('PDK025', '600.00', 24, 'DST002'),
('PDK026', '600.00', 24, 'DST002'),
('PDK027', '600.00', 24, 'DST002'),
('PDK028', '600.00', 24, 'DST002'),
('PDK029', '600.00', 24, 'DST002'),
('PDK030', '600.00', 24, 'DST002'),
('PDK031', '600.00', 24, 'DST002'),
('PDK032', '600.00', 24, 'DST002'),
('PDK033', '600.00', 24, 'DST004'),
('PDK034', '600.00', 24, 'DST004'),
('PDK035', '600.00', 24, 'DST004'),
('PDK036', '1500.00', 12, 'DST002'),
('PDK037', '1500.00', 12, 'DST002'),
('PDK038', '1500.00', 12, 'DST002'),
('PDK039', '1500.00', 12, 'DST002'),
('PDK040', '1500.00', 12, 'DST002'),
('PDK041', '1500.00', 12, 'DST004'),
('PDK042', '1500.00', 12, 'DST004'),
('PDK043', '1500.00', 12, 'DST004'),
('PDK044', '1500.00', 12, 'DST004'),
('PDK045', '1500.00', 12, 'DST004'),
('PDK054', '600.00', 24, 'DST004'),
('PDK055', '600.00', 24, 'DST004'),
('PDK056', '600.00', 24, 'DST004'),
('PDK057', '600.00', 24, 'DST004'),
('PDK058', '600.00', 24, 'DST004'),
('PDK059', '600.00', 24, 'DST004'),
('PDK060', '600.00', 24, 'DST004'),
('PDK061', '600.00', 24, 'DST006'),
('PDK062', '600.00', 24, 'DST006'),
('PDK063', '600.00', 24, 'DST006'),
('PDK064', '600.00', 24, 'DST006'),
('PDK065', '600.00', 24, 'DST006'),
('PDK066', '600.00', 24, 'DST006'),
('PDK067', '600.00', 24, 'DST006'),
('PDK068', '600.00', 24, 'DST006'),
('PDK069', '1500.00', 12, 'DST004'),
('PDK070', '1500.00', 12, 'DST004'),
('PDK071', '1500.00', 12, 'DST004'),
('PDK072', '1500.00', 12, 'DST004'),
('PDK073', '1500.00', 12, 'DST004'),
('PDK074', '1500.00', 12, 'DST006'),
('PDK075', '1500.00', 12, 'DST006'),
('PDK076', '1500.00', 12, 'DST006'),
('PDK077', '1500.00', 12, 'DST006'),
('PDK078', '1500.00', 12, 'DST006'),
('PDK079', '1500.00', 12, 'DST006'),
('PDK080', '1500.00', 12, 'DST006'),
('PDK091', '600.00', 24, 'DST006'),
('PDK092', '600.00', 24, 'DST006'),
('PDK093', '600.00', 24, 'DST006'),
('PDK094', '600.00', 24, 'DST006'),
('PDK095', '600.00', 24, 'DST006'),
('PDK096', '600.00', 24, 'DST006'),
('PDK097', '600.00', 24, 'DST006'),
('PDK098', '600.00', 24, 'DST007'),
('PDK099', '600.00', 24, 'DST007'),
('PDK100', '600.00', 24, 'DST007'),
('PDK101', '600.00', 24, 'DST007'),
('PDK102', '600.00', 24, 'DST007'),
('PDK103', '600.00', 24, 'DST007'),
('PDK104', '600.00', 24, 'DST007'),
('PDK105', '600.00', 24, 'DST007'),
('PDK106', '600.00', 24, 'DST007'),
('PDK107', '600.00', 24, 'DST007'),
('PDK108', '600.00', 24, 'DST007'),
('PDK109', '600.00', 24, 'DST007'),
('PDK110', '600.00', 24, 'DST007'),
('PDK111', '600.00', 24, 'DST007'),
('PDK112', '600.00', 24, 'DST007'),
('PDK113', '600.00', 24, 'DST007'),
('PDK114', '600.00', 24, 'DST007'),
('PDK115', '600.00', 24, 'DST007'),
('PDK116', '1500.00', 12, 'DST006'),
('PDK117', '1500.00', 12, 'DST006'),
('PDK118', '1500.00', 12, 'DST006'),
('PDK119', '1500.00', 12, 'DST009'),
('PDK120', '1500.00', 12, 'DST009'),
('PDK121', '1500.00', 12, 'DST009'),
('PDK122', '1500.00', 12, 'DST009'),
('PDK123', '1500.00', 12, 'DST009'),
('PDK124', '1500.00', 12, 'DST009'),
('PDK125', '1500.00', 12, 'DST009'),
('PDK126', '1500.00', 12, 'DST009'),
('PDK127', '1500.00', 12, 'DST009'),
('PDK128', '1500.00', 12, 'DST009'),
('PDK129', '1500.00', 12, 'DST009'),
('PDK130', '1500.00', 12, 'DST009'),
('PDK137', '600.00', 24, 'DST007'),
('PDK138', '600.00', 24, 'DST007'),
('PDK139', '600.00', 24, NULL),
('PDK140', '600.00', 24, 'DST011'),
('PDK141', '600.00', 24, 'DST011'),
('PDK142', '600.00', 24, 'DST011'),
('PDK143', '600.00', 24, 'DST011'),
('PDK144', '600.00', 24, 'DST011'),
('PDK145', '600.00', 24, 'DST011'),
('PDK146', '600.00', 24, 'DST011'),
('PDK147', '600.00', 24, 'DST011'),
('PDK148', '600.00', 24, 'DST013'),
('PDK149', '600.00', 24, 'DST013'),
('PDK150', '600.00', 24, 'DST013'),
('PDK151', '600.00', 24, 'DST013'),
('PDK152', '600.00', 24, 'DST013'),
('PDK153', '600.00', 24, 'DST013'),
('PDK154', '600.00', 24, 'DST013'),
('PDK155', '600.00', 24, 'DST013'),
('PDK156', '600.00', 24, 'DST013'),
('PDK157', '600.00', 24, 'DST013'),
('PDK158', '600.00', 24, NULL),
('PDK159', '600.00', 24, NULL),
('PDK160', '600.00', 24, NULL),
('PDK161', '600.00', 24, NULL),
('PDK162', '600.00', 24, NULL),
('PDK163', '600.00', 24, NULL),
('PDK164', '600.00', 24, NULL),
('PDK165', '600.00', 24, NULL),
('PDK166', '600.00', 24, NULL),
('PDK167', '1500.00', 12, 'DST014'),
('PDK168', '1500.00', 12, 'DST014'),
('PDK169', '1500.00', 12, 'DST014'),
('PDK170', '1500.00', 12, 'DST014'),
('PDK171', '1500.00', 12, 'DST014'),
('PDK172', '1500.00', 12, 'DST014'),
('PDK173', '1500.00', 12, 'DST014'),
('PDK174', '1500.00', 12, 'DST014'),
('PDK175', '1500.00', 12, 'DST014'),
('PDK176', '1500.00', 12, 'DST014'),
('PDK177', '1500.00', 12, 'DST014'),
('PDK178', '1500.00', 12, 'DST014'),
('PDK179', '1500.00', 12, 'DST014'),
('PDK180', '1500.00', 12, 'DST014'),
('PDK181', '1500.00', 12, NULL),
('PDK182', '1500.00', 12, NULL),
('PDK183', '1500.00', 12, NULL),
('PDK184', '1500.00', 12, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `produk_galon`
--

CREATE TABLE `produk_galon` (
  `id_produk` varchar(15) NOT NULL,
  `volume_liter` decimal(12,2) NOT NULL,
  `jenis_galon` varchar(25) NOT NULL,
  `id_distribusi` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `produk_galon`
--

INSERT INTO `produk_galon` (`id_produk`, `volume_liter`, `jenis_galon`, `id_distribusi`) VALUES
('PDK011', '19.00', 'Isi Ulang', 'DST001'),
('PDK012', '19.00', 'Isi Ulang', 'DST001'),
('PDK013', '19.00', 'Isi Ulang', 'DST001'),
('PDK014', '19.00', 'Isi Ulang', 'DST003'),
('PDK015', '19.00', 'Isi Ulang', 'DST003'),
('PDK046', '19.00', 'Isi Ulang', 'DST003'),
('PDK047', '19.00', 'Isi Ulang', 'DST003'),
('PDK048', '19.00', 'Isi Ulang', 'DST003'),
('PDK049', '19.00', 'Isi Ulang', 'DST005'),
('PDK050', '19.00', 'Isi Ulang', 'DST005'),
('PDK051', '19.00', 'Isi Ulang', 'DST005'),
('PDK052', '19.00', 'Isi Ulang', 'DST005'),
('PDK053', '19.00', 'Isi Ulang', 'DST005'),
('PDK081', '19.00', 'Isi Ulang', 'DST005'),
('PDK082', '19.00', 'Isi Ulang', 'DST008'),
('PDK083', '19.00', 'Isi Ulang', 'DST008'),
('PDK084', '19.00', 'Isi Ulang', 'DST008'),
('PDK085', '19.00', 'Isi Ulang', 'DST008'),
('PDK086', '19.00', 'Isi Ulang', 'DST008'),
('PDK087', '19.00', 'Isi Ulang', 'DST008'),
('PDK088', '19.00', 'Isi Ulang', 'DST008'),
('PDK089', '19.00', 'Isi Ulang', 'DST008'),
('PDK090', '19.00', 'Isi Ulang', 'DST008'),
('PDK131', '19.00', 'Isi Ulang', 'DST008'),
('PDK132', '19.00', 'Isi Ulang', 'DST012'),
('PDK133', '19.00', 'Isi Ulang', 'DST012'),
('PDK134', '19.00', 'Isi Ulang', 'DST012'),
('PDK135', '19.00', 'Isi Ulang', 'DST012'),
('PDK136', '19.00', 'Isi Ulang', 'DST012'),
('PDK185', '19.00', 'Isi Ulang', 'DST012'),
('PDK186', '19.00', 'Isi Ulang', 'DST012'),
('PDK187', '19.00', 'Isi Ulang', 'DST012'),
('PDK188', '19.00', 'Isi Ulang', 'DST012'),
('PDK189', '19.00', 'Isi Ulang', 'DST012'),
('PDK190', '19.00', 'Isi Ulang', 'DST012'),
('PDK191', '19.00', 'Isi Ulang', NULL),
('PDK192', '19.00', 'Isi Ulang', NULL),
('PDK193', '19.00', 'Isi Ulang', NULL),
('PDK194', '19.00', 'Isi Ulang', NULL),
('PDK195', '5.00', 'Sekali Pakai', 'DST003'),
('PDK196', '5.00', 'Sekali Pakai', 'DST003'),
('PDK197', '5.00', 'Sekali Pakai', 'DST003'),
('PDK198', '5.00', 'Sekali Pakai', 'DST003'),
('PDK199', '5.00', 'Sekali Pakai', 'DST003'),
('PDK200', '5.00', 'Sekali Pakai', 'DST003'),
('PDK201', '5.00', 'Sekali Pakai', 'DST003'),
('PDK202', '5.00', 'Sekali Pakai', 'DST003'),
('PDK203', '5.00', 'Sekali Pakai', 'DST005'),
('PDK204', '5.00', 'Sekali Pakai', 'DST005'),
('PDK205', '5.00', 'Sekali Pakai', 'DST005'),
('PDK206', '5.00', 'Sekali Pakai', 'DST005'),
('PDK207', '5.00', 'Sekali Pakai', 'DST010'),
('PDK208', '5.00', 'Sekali Pakai', 'DST010'),
('PDK209', '5.00', 'Sekali Pakai', 'DST010'),
('PDK210', '5.00', 'Sekali Pakai', 'DST010'),
('PDK211', '5.00', 'Sekali Pakai', 'DST010'),
('PDK212', '5.00', 'Sekali Pakai', 'DST015'),
('PDK213', '5.00', 'Sekali Pakai', 'DST015'),
('PDK214', '5.00', 'Sekali Pakai', 'DST015'),
('PDK215', '5.00', 'Sekali Pakai', 'DST015'),
('PDK216', '5.00', 'Sekali Pakai', 'DST015');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `agen`
--
ALTER TABLE `agen`
  ADD PRIMARY KEY (`id_agen`);

--
-- Indexes for table `detail_distribusi`
--
ALTER TABLE `detail_distribusi`
  ADD PRIMARY KEY (`id_detail_distribusi`),
  ADD UNIQUE KEY `uq_agen_distribusi_produk` (`id_agen`,`id_distribusi`,`jenis_produk`),
  ADD KEY `id_distribusi` (`id_distribusi`);

--
-- Indexes for table `distribusi`
--
ALTER TABLE `distribusi`
  ADD PRIMARY KEY (`id_distribusi`),
  ADD KEY `Petugas_Gudang_id_petugas` (`Petugas_Gudang_id_petugas`),
  ADD KEY `Petugas_Distribusi_id_petugas` (`Petugas_Distribusi_id_petugas`);

--
-- Indexes for table `petugas`
--
ALTER TABLE `petugas`
  ADD PRIMARY KEY (`id_petugas`);

--
-- Indexes for table `petugas_admin`
--
ALTER TABLE `petugas_admin`
  ADD PRIMARY KEY (`id_petugas`);

--
-- Indexes for table `petugas_distribusi`
--
ALTER TABLE `petugas_distribusi`
  ADD PRIMARY KEY (`id_petugas`);

--
-- Indexes for table `petugas_gudang`
--
ALTER TABLE `petugas_gudang`
  ADD PRIMARY KEY (`id_petugas`);

--
-- Indexes for table `petugas_produksi`
--
ALTER TABLE `petugas_produksi`
  ADD PRIMARY KEY (`id_petugas`);

--
-- Indexes for table `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`id_produk`),
  ADD KEY `Produksi_id_produksi` (`Produksi_id_produksi`);

--
-- Indexes for table `produksi`
--
ALTER TABLE `produksi`
  ADD PRIMARY KEY (`id_produksi`),
  ADD KEY `Petugas_Produksi_id_petugas` (`Petugas_Produksi_id_petugas`);

--
-- Indexes for table `produk_botol`
--
ALTER TABLE `produk_botol`
  ADD PRIMARY KEY (`id_produk`),
  ADD KEY `produk_botol_ibfk_2` (`id_distribusi`);

--
-- Indexes for table `produk_galon`
--
ALTER TABLE `produk_galon`
  ADD PRIMARY KEY (`id_produk`),
  ADD KEY `produk_galon_ibfk_2` (`id_distribusi`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `detail_distribusi`
--
ALTER TABLE `detail_distribusi`
  ADD CONSTRAINT `detail_distribusi_ibfk_1` FOREIGN KEY (`id_agen`) REFERENCES `agen` (`id_agen`),
  ADD CONSTRAINT `detail_distribusi_ibfk_2` FOREIGN KEY (`id_distribusi`) REFERENCES `distribusi` (`id_distribusi`);

--
-- Constraints for table `distribusi`
--
ALTER TABLE `distribusi`
  ADD CONSTRAINT `distribusi_ibfk_1` FOREIGN KEY (`Petugas_Gudang_id_petugas`) REFERENCES `petugas_gudang` (`id_petugas`),
  ADD CONSTRAINT `distribusi_ibfk_2` FOREIGN KEY (`Petugas_Distribusi_id_petugas`) REFERENCES `petugas_distribusi` (`id_petugas`);

--
-- Constraints for table `petugas_admin`
--
ALTER TABLE `petugas_admin`
  ADD CONSTRAINT `petugas_admin_ibfk_1` FOREIGN KEY (`id_petugas`) REFERENCES `petugas` (`id_petugas`);

--
-- Constraints for table `petugas_distribusi`
--
ALTER TABLE `petugas_distribusi`
  ADD CONSTRAINT `petugas_distribusi_ibfk_1` FOREIGN KEY (`id_petugas`) REFERENCES `petugas` (`id_petugas`);

--
-- Constraints for table `petugas_gudang`
--
ALTER TABLE `petugas_gudang`
  ADD CONSTRAINT `petugas_gudang_ibfk_1` FOREIGN KEY (`id_petugas`) REFERENCES `petugas` (`id_petugas`);

--
-- Constraints for table `petugas_produksi`
--
ALTER TABLE `petugas_produksi`
  ADD CONSTRAINT `petugas_produksi_ibfk_1` FOREIGN KEY (`id_petugas`) REFERENCES `petugas` (`id_petugas`);

--
-- Constraints for table `produk`
--
ALTER TABLE `produk`
  ADD CONSTRAINT `produk_ibfk_1` FOREIGN KEY (`Produksi_id_produksi`) REFERENCES `produksi` (`id_produksi`);

--
-- Constraints for table `produksi`
--
ALTER TABLE `produksi`
  ADD CONSTRAINT `produksi_ibfk_1` FOREIGN KEY (`Petugas_Produksi_id_petugas`) REFERENCES `petugas_produksi` (`id_petugas`);

--
-- Constraints for table `produk_botol`
--
ALTER TABLE `produk_botol`
  ADD CONSTRAINT `produk_botol_ibfk_1` FOREIGN KEY (`id_produk`) REFERENCES `produk` (`id_produk`),
  ADD CONSTRAINT `produk_botol_ibfk_2` FOREIGN KEY (`id_distribusi`) REFERENCES `distribusi` (`id_distribusi`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `produk_galon`
--
ALTER TABLE `produk_galon`
  ADD CONSTRAINT `produk_galon_ibfk_1` FOREIGN KEY (`id_produk`) REFERENCES `produk` (`id_produk`),
  ADD CONSTRAINT `produk_galon_ibfk_2` FOREIGN KEY (`id_distribusi`) REFERENCES `distribusi` (`id_distribusi`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
