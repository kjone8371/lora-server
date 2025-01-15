package com.dipvision.lora.common.permission

@JvmInline
value class Permission(private val value: Long) : Iterable<Permissions> {
    operator fun plus(permission: Permission) = Permission(this.value or permission.value)
    operator fun minus(permission: Permission) = Permission(this.value and permission.value.inv())
    
    operator fun contains(permission: Permissions) = this.value and permission.permission.value == permission.permission.value
    operator fun contains(permission: Permission) = this.value and permission.value == permission.value
    operator fun contains(permission: Long) = this.value and permission == permission

    override fun iterator(): Iterator<Permissions> {
        return iterator {
            for (entry in Permissions.entries) {
                if (entry.permission in this@Permission) yield(entry)
            }
        }
    }

    companion object {
        operator fun Long.contains(permission: Permission): Boolean {
            return permission.value and this == permission.value
        }
    }
}