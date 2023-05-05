FROM nginx
COPY ./default.conf /etc/nginx/conf.d/default.conf
RUN apt update -y
RUN apt install -y certbot python3-certbot-nginx

EXPOSE 80 443