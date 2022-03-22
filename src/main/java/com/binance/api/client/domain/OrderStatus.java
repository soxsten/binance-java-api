package com.binance.api.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Status of a submitted order.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public enum OrderStatus {
  /** Новый. */
  NEW,
  /** Частично исполнен. */
  PARTIALLY_FILLED,
  /** Исполнен. */
  FILLED,
  /** Отменен. */
  CANCELED,
  /** Ожидание отмены. */
  PENDING_CANCEL,
  /** Отклонен. */
  REJECTED,
  /** Истек. */
  EXPIRED
}
