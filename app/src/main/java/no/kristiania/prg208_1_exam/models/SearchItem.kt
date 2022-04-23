package no.kristiania.prg208_1_exam.models

import android.net.Uri

class SearchItem(// here i am taking only image url. and this is as integer because i am using it from drawable file.
    var itemId: Int, var imageUri: Uri, val original: Boolean
)