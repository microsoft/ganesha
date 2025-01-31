

document.addEventListener('DOMContentLoaded', () => {
    var currentConversation = {
        id: null,
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

        var updatedConversation = await sendMessageToServer(currentConversation);

        currentConversation = updatedConversation;

        displayConversation(currentConversation.messages);
    }

    function displayConversation(messages) {
        const chatMessages = document.getElementById('messages');
        chatMessages.innerHTML = '';
        messages.forEach(msg => {
            displayMessage(msg.role, msg.message, msg.time);
        });
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
});
