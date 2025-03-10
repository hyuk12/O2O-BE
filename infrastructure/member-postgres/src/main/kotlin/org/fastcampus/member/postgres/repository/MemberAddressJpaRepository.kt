package org.fastcampus.member.postgres.repository

import org.fastcampus.member.code.MemberAddressType
import org.fastcampus.member.postgres.entity.MemberAddressJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberAddressJpaRepository : JpaRepository<MemberAddressJpaEntity, Long> {
    fun countByUserIdAndMemberAddressTypeAndIsDeleted(userId: Long, memberAddressType: MemberAddressType, isDeleted: Boolean): Long

    fun findByUserId(userId: Long): List<MemberAddressJpaEntity>

    fun findByUserIdAndIsDefault(userId: Long, isDefault: Boolean): Optional<MemberAddressJpaEntity>

    fun findByIdAndUserId(addressId: Long, userId: Long): Optional<MemberAddressJpaEntity>
}
