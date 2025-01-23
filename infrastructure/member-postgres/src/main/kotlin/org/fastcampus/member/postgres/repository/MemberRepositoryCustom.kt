package org.fastcampus.member.postgres.repository

import org.fastcampus.applicationclient.config.security.exception.UserNotFoundException
import org.fastcampus.member.code.Role
import org.fastcampus.member.entity.Member
import org.fastcampus.member.postgres.entity.toJpaEntity
import org.fastcampus.member.postgres.entity.toModel
import org.fastcampus.member.repository.MemberRepository
import org.springframework.stereotype.Repository

/**
 * Created by kms0902 on 25. 1. 19..
 */
@Repository
class MemberRepositoryCustom(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {
    override fun save(member: Member): Member {
        return memberJpaRepository.save(member.toJpaEntity()).toModel()
    }

    override fun findBySignname(signname: String): Member {
        return memberJpaRepository.findBySignname(
            signname,
        ).map { it.toModel() }.orElseThrow { UserNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다") }
    }

    override fun findByRoleAndSignname(role: String, signname: String): Member {
        return memberJpaRepository.findByRoleAndSignname(Role.valueOf(role), signname).map {
            it.toModel()
        }.orElseThrow { UserNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다") }
    }
}
