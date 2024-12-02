<?php
    $nama_database = "db_apkmenajemenkeuangan";
    $user_name_db  = "root";
    $pwd_db        = "";
    $server_host   = "localhost";

    $connection    = mysqli_connect($server_host,$user_name_db,$pwd_db,$nama_database);

    //if($connection){
    //    echo "koneksi berhasil";
    //}else {
     //   echo"koneksi gagal";
    //}
?>