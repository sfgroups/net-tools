FROM nginx:alpine

ENV TERM=xterm \
    PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:${PATH}"

RUN apk add --no-cache iproute2 jq nmap wget tcpdump bash curl bind-tools openssl supervisor openssh openssh-server mtr procps && \
    mkdir -p /run/nginx /var/run/sshd /var/log/supervisor  /root/.ssh /var/www/html && \
    echo 'root:password' | chpasswd

COPY server_run.sh /sbin
RUN mkdir -p -m 755 /etc/supervisor.d
COPY supervisor-nginx.ini supervisor-sshd.ini supervisor.ini /etc/supervisor.d/
RUN chmod a+x /sbin/server_run.sh

COPY sshd_config /etc/ssh/
RUN ssh-keygen -A

COPY nginx/nginx.conf /etc/nginx/nginx.conf
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
COPY nginx/index.html /var/www/html
RUN chown -R nginx:nginx /var/www/html

# forward request and error logs to docker log collector
RUN ln -sf /dev/stdout /var/log/nginx/access.log \
	&& ln -sf /dev/stderr /var/log/nginx/error.log

STOPSIGNAL SIGTERM

EXPOSE 22 80 443
CMD ["/sbin/server_run.sh"]
