<?php
require "../config/config.php"; // Pastikan ada titik koma di akhir baris ini

if ($_SERVER['REQUEST_METHOD'] === "POST") {
    if (isset($_POST['nama_user']) && isset($_POST['email_active']) && isset($_POST['username']) && isset($_POST['password'])) { // Penulisan isset diperbaiki
        $str_namauser = $_POST['nama_user'];
        $str_email = $_POST['email_active'];
        $str_username = $_POST['username'];
        $str_password = $_POST['password'];

        // Query pengecekan data user di database
        $QUERY_CEK_USER_EXIST = "SELECT COUNT(id) as user_exist FROM tbl_users WHERE user_name = '$str_namauser' AND user_password = '$str_password' OR kontak_email = '$str_email';";
        $EXECUTE_QUERY_EXIST_USER = mysqli_query($connection, $QUERY_CEK_USER_EXIST);

        if ($EXECUTE_QUERY_EXIST_USER) { // Validasi apakah query berhasil
            $FETCH_ROW_USER_EXIST = mysqli_fetch_row($EXECUTE_QUERY_EXIST_USER);

            if ($FETCH_ROW_USER_EXIST[0]) { // Periksa apakah jumlah user lebih dari 0
                $response['code'] = 505;
                $response['status'] = false;
                $response['message'] = "Opps, sory data user sudah ada dalam sistem";
            } else {
                // Query menambahkan data user dalam database
                $QUERY_TAMBAH_USER = "INSERT INTO tbl_users (nama_user, kontak_email, user_name, user_password) VALUES ('$str_namauser', '$str_email', '$str_username', '$str_password');";
                $EXECUTE_TAMBAH_USER = mysqli_query($connection, $QUERY_TAMBAH_USER);

                if ($EXECUTE_TAMBAH_USER) { // Validasi apakah query berhasil
                    // Query untuk fetching database
                    $QUERY_FETCH_DATA_USER = "SELECT * FROM tbl_users WHERE user_name = '$str_username' AND user_password = '$str_password';";
                    $EXECUTE_FETCH_DATA_USER = mysqli_query($connection, $QUERY_FETCH_DATA_USER);

                    if ($EXECUTE_FETCH_DATA_USER) { // Validasi apakah query fetch berhasil
                        $FETCH_DATA_USER = mysqli_fetch_row($EXECUTE_FETCH_DATA_USER);

                        if ($FETCH_DATA_USER) { // Pastikan hasil fetch tidak kosong
                            $response['code'] = 200;
                            $response['status'] = true;
                            $response['message'] = "Yey, data user berhasil ditambahkan";

                            $response['data_user'] = [ // Mengubah "data user" menjadi "data_user"
                                "id" => $FETCH_DATA_USER[0],
                                "name_user" => $FETCH_DATA_USER[1],
                                "email" => $FETCH_DATA_USER[2],
                                "username" => $FETCH_DATA_USER[3],
                                "password" => $FETCH_DATA_USER[4],
                            ];
                        } else {
                            $response['code'] = 500;
                            $response['status'] = false;
                            $response['message'] = "Gagal mengambil data user yang baru ditambahkan.";
                        }
                    } else {
                        $response['code'] = 500;
                        $response['status'] = false;
                        $response['message'] = "Query fetch data user gagal.";
                    }
                } else {
                    $response['code'] = 500;
                    $response['status'] = false;
                    $response['message'] = "Ups, gagal menambah data ke dalam sistem.";
                }
            }
        } else {
            $response['code'] = 500;
            $response['status'] = false;
            $response['message'] = "Query cek user gagal.";
        }

        echo json_encode($response);
    }
} else {
    $response['code'] = 502;
    $response['status'] = false;
    $response['message'] = "Sorry, anda harus menggunakan method POST untuk mengeksekusi API ini.";

    echo json_encode($response);
}
?>
