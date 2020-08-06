package com.jssk.android.dtos

import com.google.firebase.firestore.Exclude
import java.util.*
import kotlin.collections.HashMap

class UserDTO {
    @get:Exclude
    var id: String? = null
    var name: String? = null
    var mobile: String? = null
    var email: String? = null
    var address: String? = null
    var mobile2: String? = null
    var dob: String? = null
    var role: String? = null
    var enabled: Boolean? = null
    var verified: Boolean? = null
    var drivingSince: String? = null
    var hadInsurance: Boolean? = null
    var spouseName: String? = null
    var fatherName: String? = null
    var motherName: String? = null
    var createdAt: Long? = null

    constructor()

    constructor(role: String, name: String, mobile: String, mobile2: String,
                email: String, address: String) {
        this.role = role
        this.name = name
        this.mobile = mobile
        this.mobile2 = mobile2
        this.email = email
        this.address = address
        enabled = true
        verified= true
        createdAt = Calendar.getInstance().timeInMillis
    }

    fun getMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        if(role != null)
            map["role"] = role!!
        if(name != null)
            map["name"] = name!!
        if(mobile != null)
            map["mobile"] = mobile!!
        if(mobile2 != null)
            map["mobile2"] = mobile2!!
        if(email != null)
            map["email"] = email!!
        if(address != null)
            map["address"] = address!!
        if(dob != null)
            map["dob"] = dob!!
        if(enabled != null)
            map["enabled"] = enabled!!
        if(verified != null)
            map["verified"] = verified!!
        if(createdAt != null)
            map["createdAt"] = createdAt!!
        return map
    }
}