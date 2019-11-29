package com.example.filemanager.Entity

import org.litepal.crud.LitePalSupport

class User : LitePalSupport() {
    var password: String? = null
}
