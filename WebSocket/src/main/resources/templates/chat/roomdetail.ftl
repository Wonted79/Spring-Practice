<!doctype html>
<html lang="en">
<head>
    <title>Websocket ChatRoom</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css">
    <style>[v-cloak] { display: none; }</style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <h2>{{room.name}}</h2>
    <div class="input-group">
        <div class="input-group-prepend">
            <label class="input-group-text">내용</label>
        </div>
        <input type="text" class="form-control" v-model="message" v-on:keypress.enter="sendMessage">
        <div class="input-group-append">
            <button class="btn btn-primary" type="button" @click="sendMessage">보내기</button>
        </div>
    </div>
    <ul class="list-group">
        <li class="list-group-item" v-for="message in messages">
            {{message.sender}} - {{message.message}}
        </li>
    </ul>
</div>
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
<script>
    var sock = new SockJS("/ws-stomp");
    var ws = Stomp.over(sock);
    var reconnect = 0;

    var vm = new Vue({
        el: '#app',
        data: { roomId: '', room: {}, sender: '', message: '', messages: [] },
        created() {
            this.roomId = localStorage.getItem('wschat.roomId');
            this.sender = localStorage.getItem('wschat.sender');
            this.findRoom();
        },
        methods: {
            findRoom: function() {
                axios.get('/chat/room/' + this.roomId).then(response => { this.room = response.data; });
            },
            sendMessage: function() {
                ws.send("/pub/chat/message", {}, JSON.stringify({
                    type: 'TALK', roomId: this.roomId, sender: this.sender, message: this.message
                }));
                this.message = '';
            },
            recvMessage: function(recv) {
                this.messages.unshift({
                    type: recv.type,
                    sender: recv.type === 'ENTER' ? '[알림]' : recv.sender,
                    message: recv.message
                });
            }
        }
    });

    function connect() {
        ws.connect({}, function(frame) {
            // 이 채팅방 구독
            ws.subscribe("/sub/chat/room/" + vm.$data.roomId, function(message) {
                var recv = JSON.parse(message.body);
                vm.recvMessage(recv);
            });
            // 입장 메시지 전송
            ws.send("/pub/chat/message", {}, JSON.stringify({
                type: 'ENTER', roomId: vm.$data.roomId, sender: vm.$data.sender
            }));
        }, function(error) {
            // 연결 실패 시 재시도 (최대 5회)
            if (reconnect++ <= 5) {
                setTimeout(function() {
                    sock = new SockJS("/ws-stomp");
                    ws = Stomp.over(sock);
                    connect();
                }, 10 * 1000);
            }
        });
    }
    connect();
</script>
</body>
</html>