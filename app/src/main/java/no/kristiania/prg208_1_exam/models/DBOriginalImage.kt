package no.kristiania.prg208_1_exam.models


class DBOriginalImage(
    val id: Int?,
    val byteArray: ByteArray?,
    val created: String?
) {
    override fun toString(): String {
        return "DBOriginalImage(id=$id, byteArray=${byteArray?.contentToString()}, created=$created)"
    }
}
