    package com.example.admin_duan1.dto;

    import java.util.List;

    public class Request {
        private String user, phone, address, total, status;
        List<OderDetail> listOder; //Danh sách Food Oder;


        public Request() {
        }

        public Request(String user, String phone, String address, String total, List<OderDetail> listOder) {
            this.user = user;
            this.phone = phone;
            this.address = address;
            this.total = total;
            this.listOder = listOder;
            this.status = "0"; //Cho = 0; Đặt hàng =0,Vận chuyển = 1,Đang vận chuyển = 2;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<OderDetail> getListOder() {
            return listOder;
        }

        public void setListOder(List<OderDetail> listOder) {
            this.listOder = listOder;
        }
    }
