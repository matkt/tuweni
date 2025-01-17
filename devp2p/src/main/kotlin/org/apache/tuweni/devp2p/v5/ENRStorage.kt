// Copyright The Tuweni Authors
// SPDX-License-Identifier: Apache-2.0
package org.apache.tuweni.devp2p.v5

import org.apache.tuweni.bytes.Bytes
import org.apache.tuweni.crypto.Hash
import org.apache.tuweni.devp2p.EthereumNodeRecord
import java.util.concurrent.ConcurrentHashMap

/**
 * Storage of node records
 */
interface ENRStorage {

  /**
   * Add an ENR record to the store
   *
   * @param enr node record
   */
  fun set(enr: EthereumNodeRecord) {
    val nodeId = Hash.sha2_256(enr.toRLP())
    put(nodeId, enr)
  }

  /**
   * Store an ENR record associated with a nodeId in the store.
   */
  fun put(nodeId: Bytes, enr: EthereumNodeRecord)

  /**
   * Find a stored node record
   *
   * @param nodeId node identifier
   *
   * @return node record, if present.
   */
  fun find(nodeId: Bytes): EthereumNodeRecord?
}

/**
 * Default storage for Ethereum Node Records, backed by an in-memory hash map.
 */
internal class DefaultENRStorage : ENRStorage {

  private val storage: MutableMap<Bytes, EthereumNodeRecord> = ConcurrentHashMap()

  override fun find(nodeId: Bytes): EthereumNodeRecord? = storage[nodeId]

  override fun put(nodeId: Bytes, enr: EthereumNodeRecord) { storage.put(nodeId, enr) }
}
