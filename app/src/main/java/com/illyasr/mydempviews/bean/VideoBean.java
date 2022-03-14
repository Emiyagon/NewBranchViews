package com.illyasr.mydempviews.bean;

import java.util.List;

public class VideoBean {

    /**
     * success : true
     * code : 0
     * message : null
     * title : 民族舞【白马】
     * desc :
     * author : 简艺术空间
     * coverPic : http://i1.hdslb.com/bfs/archive/7f5eb1f525f26d0a734ff4065a24ece29f356390.jpg
     * resources : [{"originalUrl":"https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/98/29/359472998/359472998-1-208.mp4?e=ig8euxZM2rNcNbRznwdVhwdlhWh3hwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1625311692&gen=playurlv2&os=cosbv&oi=1386025869&trid=1c0bc95789e543938aeb97a9833b1033T&platform=html5&upsig=e08d6647a87dc67b790fd2dc104551a6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&bvc=vod&orderid=0,1&logo=80000000","duration":"02:34","size":"21376443","format":"MP4","type":"VIDEO","ratio":"高清 1080P"},{"originalUrl":"https://upos-sz-mirrorkodo.bilivideo.com/upgcxcode/98/29/359472998/359472998-1-16.mp4?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1625304492&gen=playurlv2&os=kodobv&oi=1386025869&trid=b88d3ee4336643d9873e5105619e4b8bh&platform=html5&upsig=baf132cb92ecbd6b77f5f17fdc3fc970&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&logo=80000000","duration":"02:33","size":"8220373","format":"MP4","type":"VIDEO","ratio":"流畅 360P"}]
     */

    private boolean success;
    private int code;
    private Object message;
    private String title;
    private String desc;
    private String author;
    private String coverPic;
    /**
     * originalUrl : https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/98/29/359472998/359472998-1-208.mp4?e=ig8euxZM2rNcNbRznwdVhwdlhWh3hwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1625311692&gen=playurlv2&os=cosbv&oi=1386025869&trid=1c0bc95789e543938aeb97a9833b1033T&platform=html5&upsig=e08d6647a87dc67b790fd2dc104551a6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&bvc=vod&orderid=0,1&logo=80000000
     * duration : 02:34
     * size : 21376443
     * format : MP4
     * type : VIDEO
     * ratio : 高清 1080P
     */

    private List<ResourcesBean> resources;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public List<ResourcesBean> getResources() {
        return resources;
    }

    public void setResources(List<ResourcesBean> resources) {
        this.resources = resources;
    }

    public static class ResourcesBean {
        private String originalUrl;
        private String duration;
        private String size;
        private String format;
        private String type;
        private String ratio;

        public String getOriginalUrl() {
            return originalUrl;
        }

        public void setOriginalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }
    }
}
