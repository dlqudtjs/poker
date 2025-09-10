package mong.poker.webapi.global.response

class ApiSuccessListResponse(
    val pageSize: Int,
    val page: Int,
    val data: List<Any>,
)
