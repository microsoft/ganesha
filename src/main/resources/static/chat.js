

document.addEventListener('DOMContentLoaded', () => {
    var currentConversation = {
        conversationId: null,
        messages: []
    };
    document.addEventListener('keyup', handleEnter);
    document.getElementById('send-button').addEventListener('click', handleSubmit);
    document.getElementById('message-input').addEventListener('keyup', handleEnter);

    async function handleEnter(event) {
        if (event.key === 'Enter') {
            event.preventDefault(); // Prevent default form submission
            await handleSubmit();
        }
    }

    async function startNewConversation() {
        currentConversation = {
            conversationId: null,
            messages: []
        };
        displayConversation(currentConversation);
        const url = new URL(window.location);
        url.searchParams.delete('conversationId');
        window.history.pushState({}, '', url);
    }

    document.getElementById('new-conversation-button').addEventListener('click', startNewConversation);

    document.addEventListener('keydown', (event) => {
          if (event.ctrlKey && event.shiftKey && event.key === 'Z') {
                event.preventDefault();
                startNewConversation();
          }
     });
    document.getElementById('new-conversation-button').addEventListener('click', async () => {
        currentConversation = {
            conversationId: null,
            messages: []
        };
        displayConversation(currentConversation);
    });


    async function handleSubmit(event) {
        if (event) event.preventDefault();

        const chatInput = document.getElementById('message-input');
        const userMessage = chatInput.value;
        if (userMessage.trim() === '') return;

        // reset the input field
        chatInput.value = '';

        currentConversation.messages.push({ role: 'user', message: userMessage, time: new Date() });

        // temporarily show the user message in the chat so that the user knows that their message is being processed
        displayMessage('user', userMessage, new Date());
        displayMessage('assistant', 'thinking...', new Date()); // potentially show a spinner instead

        let isNewConvo = !currentConversation.conversationId;

        await sendMessageToServer(currentConversation);

        displayConversation(currentConversation);
        if (isNewConvo) {
            await loadConversations();
        }
    }

    function displayConversation(conversation) {

        const conversationHeader = document.getElementById('conversation-header');
        conversationHeader.innerHTML = `
            <h5 class="mb-0">Ganesha</h5>
            <h8 class="mb-0">${conversation.conversationId}</h8>
        `;

        const chatMessages = document.getElementById('messages');
        chatMessages.innerHTML = '';
        displayMessage('assistant', 'Please be advised, I am an assistant with access to this chat context. Though I am a robot and not a person, your words are still real. Please use civility in our discourse.', new Date());

        if (!conversation.messages || conversation.messages.length === 0)
            return;

        for (var i = 0; i < conversation.messages.length; i++) {
            const msg = conversation.messages[i];
            displayMessage(msg.role, msg.message, msg.time);
        };
    }

    function displayMessage(role, message, time) {
        const chatMessages = document.getElementById('messages');

        const messageContainer = document.createElement('div');
        messageContainer.className = 'd-flex flex-row justify-content-start';

        const messageContent = document.createElement('div');

        const messageText = document.createElement('p');
        messageText.className = 'small p-2 ms-3 mb-1 rounded-3 bg-body-tertiary';
        messageText.innerText = message;

        const messageTime = document.createElement('p');
        messageTime.className = 'small ms-3 mb-3 rounded-3 text-muted';
        const options = { hour: '2-digit', minute: '2-digit', hour12: true };
        messageTime.innerText = new Date(time).toLocaleTimeString('en-US', options);

        messageContent.appendChild(messageText);
        messageContent.appendChild(messageTime);

        if (role !== 'user') {
            messageContainer.className = 'd-flex flex-row justify-content-start';

            const avatar = document.createElement('img');
            avatar.src = 'https://static.vecteezy.com/system/resources/previews/046/861/635/large_2x/copilot-icon-transparent-background-free-png.png';
            avatar.alt = 'avatar 1';
            avatar.style.width = '45px';
            avatar.style.height = '100%';
            messageContainer.appendChild(avatar);
        }
        else {
            messageContainer.className = 'd-flex flex-row justify-content-end mb-4 pt-1';
        }

        messageContainer.appendChild(messageContent);

        chatMessages.appendChild(messageContainer);
    }

    async function sendMessageToServer(conversation) {
        const response = await fetch('/conversation', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(conversation)
        });

        const updatedConversation = await response.json();

        currentConversation = updatedConversation;

        return updatedConversation;
    }

    function getQueryVariable(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            if (pair[0] == variable) {
                return pair[1];
            }
        }
    }

    async function startConversation() {
        const conversationId = getQueryVariable('conversationId');
        if (conversationId) {
            const response = await fetch(`/conversation/${conversationId}`);
            const conversation = await response.json();
            currentConversation = conversation;
            displayConversation(conversation);
        }
    }


    async function loadConversations() {
        const response = await fetch('/conversation');
        const conversations = await response.json();
        const conversationsList = document.getElementById('conversations');
        conversationsList.innerHTML = '';

        for (var i = 0; i < conversations.length; i++) {
            var conversation = conversations[i];
            const listItem = document.createElement('li');
            listItem.className = 'list-group-item';

            const link = document.createElement('a');
            link.href = `?conversationId=${conversation.conversationId}`;
            link.innerText = conversation.conversationId;

            listItem.appendChild(link);
            conversationsList.appendChild(listItem);
        }

        if (conversations.length === 0) {
            const listItem = document.createElement('li');
            listItem.className = 'list-group-item';
            const span = document.createElement('span');
            const italicText = document.createElement('i');
            italicText.innerText = 'No conversations found yet!';
            span.appendChild(italicText);
            listItem.appendChild(span);
            conversationsList.appendChild(listItem);
        }
    }

    loadConversations();

    startConversation();
});
