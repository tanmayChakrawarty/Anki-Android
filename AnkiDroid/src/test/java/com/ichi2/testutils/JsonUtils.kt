/*
 *  Copyright (c) 2021 David Allison <davidallisongithub@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ichi2.testutils

import com.ichi2.utils.JSONObjectHolder
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.json.JSONObject

fun isJsonEqual(value: JSONObject) = IsJsonEqual(value)

fun isJsonEqual(value: String) = IsJsonEqual(JSONObject(value))

private fun matchesJsonValue(
    expectedValue: JSONObject,
    actualValue: JSONObject,
): Boolean {
    val expectedMap = expectedValue.keys().asSequence().associateWith { actualValue[it] }

    val itemKeys = actualValue.keys().asSequence().toList()
    val differentKeys =
        itemKeys
            .associateWith { actualValue[it] }
            .filter { expectedMap[it.key].toString() != it.value.toString() }

    return differentKeys.isEmpty() && expectedMap.size == itemKeys.size
}

// TODO: This doesn't describe the inputs in the correct order
// TODO: This should return the keys which do not match
class IsJsonEqual(
    private val expectedValue: JSONObject,
) : BaseMatcher<JSONObject>() {
    override fun describeTo(description: Description?) {
        description?.appendValue(expectedValue)
    }

    override fun matches(item: Any?): Boolean {
        if (item !is JSONObject) return false
        return matchesJsonValue(expectedValue, item)
    }
}

class IsJsonHolderEqual(
    private val expectedValue: JSONObject,
) : BaseMatcher<JSONObjectHolder>() {
    override fun matches(item: Any?): Boolean {
        if (item !is JSONObjectHolder) return false
        return matchesJsonValue(expectedValue, item.jsonObject)
    }

    override fun describeTo(description: Description?) {
        description?.appendValue(expectedValue)
    }
}

fun isJsonHolderEqual(expectedValue: String) = IsJsonHolderEqual(JSONObject(expectedValue))
