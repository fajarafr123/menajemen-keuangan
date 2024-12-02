<?php
require "../config/config.php"; // Pastikan ada titik koma di akhir baris ini

if ($_SERVER['REQUEST_METHOD'] === "POST") {
    if (isset($_POST['username']) && isset($_POST['password'])) { // Penulisan isset diperbaiki
        $str_username = $_POST['username'];
        $str_password = $_POST['password'];

        // Query pengecekan data user di database
        $QUERY_CEK_USER_EXIST = "SELECT COUNT(id) as user_exist FROM tbl_users WHERE user_name = '$str_username' AND user_password = '$str_password'";
        $EXECUTE_QUERY_EXIST_USER = mysqli_query($connection, $QUERY_CEK_USER_EXIST);
        $FETCH_ROW_USER_EXIST = mysqli_fetch_row($EXECUTE_QUERY_EXIST_USER);
       
        if ($FETCH_ROW_USER_EXIST[0]) { // Periksa apakah jumlah user adalah 0
            // Query pengecekan data user di database
            $QUERY_FETCH_DATA_USER = "SELECT * FROM tbl_users WHERE user_name = '$str_username' AND user_password = '$str_password';";
            $EXECUTE_FETCH_DATA_USER = mysqli_query($connection, $QUERY_FETCH_DATA_USER);
            $FETCH_DATA_USER = mysqli_fetch_row($EXECUTE_FETCH_DATA_USER);

            $response['code'] = 200;
            $response['status'] = true;
            $response['message'] = "yey , data user ada di database";

            $response['data user'] = [
                "id" => $FETCH_DATA_USER[0],
                "name_user" => $FETCH_DATA_USER[1],
                "email" => $FETCH_DATA_USER[2],
                "username" => $FETCH_DATA_USER[3],
                "password" => $FETCH_DATA_USER[4],
            ];

        } else {
            $response['code'] = 404;
            $response['status'] = false;
            $response['message'] = "Opps, sory data user belum ada di database";
        }
 
        echo json_encode( $response);

    }
} else {
    $response['code'] = 502;
    $response['status'] = false;
    $response['message'] = "Sorry, anda harus menggunakan method post untuk mengeksekusi API INI";
}
?>
