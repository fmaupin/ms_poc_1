package com.fmaupin.mspoc1.service.result;

import java.util.concurrent.ExecutionException;
import com.fmaupin.mspoc1.model.message.InputMessage;

/**
 * Interface pour couche service pour gestion des résultats issus traitement des
 * messages entrants
 *
 * @author fmaupin, 29/08/2023
 *
 * @since 0.0.1-SNAPSHOT
 *
 *        mspoc1 is free software; you can redistribute it and/or
 *        modify it under the terms of the GNU Lesser General Public License as
 *        published by the Free Software Foundation; either version 3 of the
 *        License, or (at your option) any later version.
 *
 *        mspoc1 is distributed in the hope that it will be
 *        useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *        Lesser General Public License for more details.
 *
 *        You should have received a copy of the GNU Lesser General Public
 *        License along with this program; if not, write to the Free Software
 *        Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *        02110-1301, USA.
 */
public interface Result {

    public void addToQueue(String msg);

    public void process(InputMessage poll)
            throws InterruptedException, ExecutionException;

    public void setToComplete(int index, InputMessage poll, String result);

    public void setToSend(int index, InputMessage poll);

    public boolean isAnyMessagesToSend();

    public void send();

    public void errorHandling(Exception e);

    public void errorHandling(String message);

    public void clean();

    public void shutdown();
}
