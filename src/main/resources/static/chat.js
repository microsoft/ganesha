

document.addEventListener('DOMContentLoaded', () => {
    const allMessages = [];
    document.addEventListener('keyup', handleEnter);
    document.getElementById('send-button').addEventListener('click', handleSubmit);
    document.getElementById('message-input').addEventListener('keyup', handleEnter);

    async function handleEnter(event) {
        if (event.key === 'Enter') {
            event.preventDefault(); // Prevent default form submission
            await handleSubmit();
        }
    }

    async function handleSubmit(event) {
        if (event) event.preventDefault();

        const chatInput = document.getElementById('message-input');
        const userMessage = chatInput.value;
        if (userMessage.trim() === '') return;

        // reset the input field
        chatInput.value = '';

        // todo: replace this part with the actual server call
        allMessages.push({ role: 'user', message: userMessage, time: new Date() });
        allMessages.push({ role: 'assistant', message: 'blah blah blah I am a robot blah blah blah', time: new Date() });

        updateChat(allMessages);
    }

    function updateChat(messages) {
        const chatMessages = document.getElementById('messages');
        chatMessages.innerHTML = '';
        messages.forEach(msg => {
            addMessageToChat(msg.role, msg.message, msg.time);
        });
    }

    function addMessageToChat(role, message, time) {
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

    // async function sendMessageToServer(message) {
    //     const response = await fetch('/chat-endpoint', {
    //       method: 'POST',
    //       headers: {
    //         'Content-Type': 'application/json'
    //       },
    //       body: JSON.stringify({
    //         conversationId: window.conversationId,
    //         message: message
    //       })
    //     });

    //     const data = await response.json();
    //     if (data.length > 0) {
    //       window.conversationId = data[0].conversationId;
    //     }
    //     return data;
    //   }
});
