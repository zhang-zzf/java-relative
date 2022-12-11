# Decorator
## Intent
## Problem

Imagine that you're working on a notification library which lets
other programs notify their users about important events.

The initial version of the library was based on the `Notifier` 
class that had only a few fields, a constructor and a single 
<!-- vim-markdown-toc Marked -->

<!-- vim-markdown-toc -->
`send` method. The method could accept a message argument from
a client and send the message to a list of emails that were passed
to the notifier via its constructor. A third-party app which acted
as a client was supposed to create and configure the notifier object
once, and use it each time something important happened.

![](.Decorator_images/91dfaace.png)
A program could use the notifier class to send notifications about
important events to a predefined set of emails.

At some point, you realize that users of the library expect more
than just email notifications. Many of them would like to receive
SMS about critical issues. Others would like to be notified on 
Facebook and, of course, the corporate users would love to get Slack notifications.

![](.Decorator_images/c614a87f.png)
Each notification type is implemented as a notifier's subclass.

How hard can that be? You extended the `Notifier` class and put the additional 
notification methods into the new subclass.
