package com.offlineupi.packet;

public class HybridEncryptedPaymentPacket {

        private String packetId;
        private String transactionId;
        private String encryptedPayload;
        private String encryptedAesKey;

        public HybridEncryptedPaymentPacket() {
        }

        public HybridEncryptedPaymentPacket(
                        String packetId,
                        String transactionId,
                        String encryptedPayload,
                        String encryptedAesKey) {

                this.packetId = packetId;
                this.transactionId = transactionId;
                this.encryptedPayload = encryptedPayload;
                this.encryptedAesKey = encryptedAesKey;
        }

        public String getPacketId() {
                return packetId;
        }

        public void setPacketId(String packetId) {
                this.packetId = packetId;
        }

        public String getTransactionId() {
                return transactionId;
        }

        public void setTransactionId(String transactionId) {
                this.transactionId = transactionId;
        }

        public String getEncryptedPayload() {
                return encryptedPayload;
        }

        public void setEncryptedPayload(String encryptedPayload) {
                this.encryptedPayload = encryptedPayload;
        }

        public String getEncryptedAesKey() {
                return encryptedAesKey;
        }

        public void setEncryptedAesKey(String encryptedAesKey) {
                this.encryptedAesKey = encryptedAesKey;
        }
}