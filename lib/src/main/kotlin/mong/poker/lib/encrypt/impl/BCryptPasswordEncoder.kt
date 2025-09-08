package mong.poker.lib.encrypt.impl

import mong.poker.lib.encrypt.PasswordEncoder

class BCryptPasswordEncoder : PasswordEncoder {
    private val passwordEncoder = org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()

    override fun encode(password: String): String {
        return passwordEncoder.encode(password)
    }

    override fun matches(
        password: String,
        encodedPassword: String,
    ): Boolean {
        return passwordEncoder.matches(password, encodedPassword)
    }
}
