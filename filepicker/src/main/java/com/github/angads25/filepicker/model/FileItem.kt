/*
 * Copyright (C) 2018 Angad Singh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.angads25.filepicker.model

/**
 * <p>
 * Created by Angad Singh on 15/11/18.
 * </p>
 */

class FileItem : Selectable(), Comparable<FileItem> {
    var type: Int = 0
    var path: String = ""
    var fileName: String = ""
    var description: String = ""

    override fun compareTo(other: FileItem): Int {
        return when {
            type < other.type -> 1
            type > other.type -> -1
            else -> fileName.compareTo(other.fileName)
        }
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            !is FileItem -> false
            else -> path == other.path
        }
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }
}