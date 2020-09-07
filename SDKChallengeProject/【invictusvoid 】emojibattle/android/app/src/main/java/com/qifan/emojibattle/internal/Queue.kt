/**
 * MIT License
 * <p>
 * Copyright (C) 2020 by Qifan YANG (@underwindfall)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.qifan.emojibattle.internal

internal class Queue<T> {
  private val data: MutableList<T> = mutableListOf()
  private val lock = Any()

  fun push(item: T) {
    synchronized(lock) {
      data.add(item)
    }
  }

  fun pop(): T {
    synchronized(lock) {
      val result = data.firstOrNull() ?: error("Queue is empty")
      data.removeAt(0)
      return result
    }
  }

  fun get(): T {
    synchronized(lock) {
      val result = data.firstOrNull() ?: error("Queue is empty")
      return result
    }
  }
  fun isNotEmpty(): Boolean = data.isNotEmpty()

  operator fun plusAssign(item: T) {
    push(item)
  }
}
