#!/bin/bash

#/sbin/link_mutable_files.py

if [ -e /sbin/prepare_magic.sh ]
then
    bash /sbin/prepare_magic.sh
fi

[ -n "$SSHD" ] || rm -f /etc/supervisor.d/supervisor-sshd.ini

/usr/bin/supervisord --nodaemon --configuration /etc/supervisord.conf
